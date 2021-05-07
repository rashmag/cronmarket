package ooo.cron.delivery.screens.main_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.LogOutReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@MainScope
class MainPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
) :
    BaseMvpPresenter<MainContract.View>(), MainContract.Presenter {

    private var marketCategories: List<MarketCategory>? = null
    private var user: User? = null

    override fun detachView() {
        super.detachView()
        mainScope.cancel()
    }

    override fun onCreateView() {

    }

    override fun onStartView() {
        mainScope.launch {
            defineAddress()
            if (marketCategories == null) {
                loadMarketCategories(dataManager.readChosenCity().id)
                showMarketCategories(marketCategories!!.first())
            }

            loadUser(dataManager.readToken())
        }
    }

    override fun onTabSelected(position: Int) {
        view?.startMarketCategoryFragment(marketCategories!![position])
    }

    override fun onClickAddress() {
        mainScope.launch {
            view?.navigateFirstAddressSelection()
        }
    }

    override fun onProfileClick() {
        if (user == null)
            view?.navigateLoginActivity()
    }

    override fun onLogInLogOutClick() {
        if (user == null)
            return view?.navigateLoginActivity() ?: Unit

        val token = dataManager.readToken()
        if (user != null && token.refreshToken.isNotEmpty()) {
            mainScope.launch {
                withErrorsHandle(
                    { dataManager.logOut(LogOutReq(token.refreshToken)).handleLogOut() },
                    { view?.showConnectionErrorScreen() },
                    { view?.showAnyErrorScreen() }
                )
            }
            return
        }
    }

    private suspend fun defineAddress() {
        val address = dataManager.readBuildingAddress()
        if (address.isNullOrBlank().not())
            view?.showSavedAddress(address!!)
    }

    private suspend fun loadMarketCategories(cityId: String) = withErrorsHandle(
        { dataManager.getMarketCategories(cityId).handleMarketCategories() },
        { view?.showConnectionErrorScreen() },
        { view?.showAnyErrorScreen() }
    )

    private suspend fun loadUser(token: RefreshableToken) {
        val response = dataManager.getUser("Bearer ${token.accessToken}")

        if (response.isSuccessful)
            return updateUser(response)
        if (response.code() == 401)
            return dataManager.refreshToken(token)
                .handleRefreshToken()

        view?.showAnyErrorScreen() ?: Unit
    }

    private fun Response<ResponseBody>.handleLogOut() {
        if (isSuccessful) {
            dataManager.removeToken()
            view?.reopenMainScreen()
        } else {
            view?.showAnyErrorScreen()
        }
    }

    private suspend fun Response<RefreshableToken>.handleRefreshToken() {
        if (isSuccessful) {
            dataManager.writeToken(body()!!)

            val userResponse = dataManager.getUser("Bearer ${body()!!.accessToken}")
            if (userResponse.isSuccessful)
                return updateUser(userResponse)
        }

        if (code() == 400 || code() == 401) {
            dataManager.removeToken()
            return view?.showNotAuthorizedMessage() ?: Unit
        }

        view?.showAnyErrorScreen() ?: Unit
    }

    private fun updateUser(response: Response<UserResponse>) {
        user = response.body()?.user

        user?.let {
            view?.showAuthorizedUser(it.name)
        }

        selectMarketCategory()
    }

    private fun Response<List<MarketCategory>>.handleMarketCategories() {
        if (isSuccessful.not() && body().isNullOrEmpty()) {
            view?.showAnyErrorScreen()
            return
        }
        marketCategories = body()!!
    }

    private fun showMarketCategories(chosenCategory: MarketCategory) {
        view?.showMarketCategories(marketCategories!!)
        view?.startMarketCategoryFragment(chosenCategory)
        view?.removeMarketCategoriesProgress()
    }

    private fun selectMarketCategory() {
        mainScope.launch {
            if (dataManager.readChosenCity().id == user?.lastDeliveryCityId) {
                val lastBoughtMarketCategoryPosition =
                    marketCategories!!.indexOfFirst { it.id == user?.lastMarketCategoryId }
                view?.selectMarketCategory(
                    if (lastBoughtMarketCategoryPosition == -1) 0
                    else lastBoughtMarketCategoryPosition
                )
            }
        }
    }
}