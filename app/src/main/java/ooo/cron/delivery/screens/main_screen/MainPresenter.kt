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
import java.lang.Exception
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
    private var user: UserResponse? = null
    private var basketPartnerId: String = DataManager.EMPTY_UUID

    override fun detachView() {
        super.detachView()
        mainScope.cancel()
    }

    override fun onCreateView() {

    }

    override fun onResumeView() {
        mainScope.launch {
            defineAddress()
            if (marketCategories == null) {
                loadMarketCategories(dataManager.readChosenCity().id)
                showMarketCategories(marketCategories!!.first())
            }

            loadUser(dataManager.readToken())
            val basketId = dataManager.readUserBasket()
            if (basketId != DataManager.EMPTY_UUID) {
                val basket = dataManager.getBasket(basketId)
                if (basket.isSuccessful) {
                    view
                    basketPartnerId = basket.body()?.partnerId ?: DataManager.EMPTY_UUID
                    view?.showContinueLastSession()
                }
            } else {
                view?.hideContinueLastSession()
            }

        }
    }

    override fun onTabSelected(position: Int) {
        view?.startMarketCategoryFragment(marketCategories!![position])
        dataManager.writeSelectedMarketCategory(marketCategories!![position])
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

        view?.showLogOutDialog()
    }

    override fun onLogOutApplied() {
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

    override fun continueLastSessionCLick() {
        view?.navigatePartnerScreen(basketPartnerId)
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

        if (response.isSuccessful) {
            updateUser(response)
            return
        }

        if (response.code() == 401 && token.accessToken.isNotEmpty() && token.refreshToken.isNotEmpty())
            return dataManager.refreshToken(token)
                .handleRefreshToken()
    }

    private fun Response<ResponseBody>.handleLogOut() {
        if (isSuccessful) {
            dataManager.removeToken()
            dataManager.removeUserBasket()
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

    private fun handleBasket(response: Response<UserResponse>) {
        writeBasketId(response)
    }

    private fun writeBasketId(response: Response<UserResponse>) =
        dataManager.writeUserBasket(response.body()?.basket?.id ?: DataManager.EMPTY_UUID)

    private fun updateUser(response: Response<UserResponse>) {
        handleBasket(response)
        showAuthorizeUser(response)
        selectMarketCategory()
    }

    private fun showAuthorizeUser(response: Response<UserResponse>) {
        user = response.body()

        user?.let {
            view?.showAuthorizedUser(it.user.name)
        }
    }

    private fun Response<List<MarketCategory>>.handleMarketCategories() {
        if (isSuccessful.not() && body().isNullOrEmpty()) {
            view?.showAnyErrorScreen()
            return
        }
        marketCategories = body()!!
    }

    private fun showMarketCategories(chosenCategory: MarketCategory) {
        dataManager.writeSelectedMarketCategory(chosenCategory)
        view?.showMarketCategories(marketCategories!!)
        view?.startMarketCategoryFragment(chosenCategory)
        view?.removeMarketCategoriesProgress()
    }

    private fun selectMarketCategory() {
        mainScope.launch {
            if (dataManager.readChosenCity().id == user?.user?.lastDeliveryCityId) {
                val lastBoughtMarketCategoryPosition =
                    marketCategories!!.indexOfFirst { it.id == user?.user?.lastMarketCategoryId }
                if (lastBoughtMarketCategoryPosition != -1)
                    dataManager.writeSelectedMarketCategory(marketCategories!![lastBoughtMarketCategoryPosition])
                view?.selectMarketCategory(
                    if (lastBoughtMarketCategoryPosition == -1) 0
                    else lastBoughtMarketCategoryPosition
                )
            }
        }
    }


    override fun getMarketCategory(): MarketCategory {
        return dataManager.readSelectedMarketCategory()
    }

    override fun onStartMarketCategory() {
        loadSpecialOrders()
    }

    private fun loadSpecialOrders() {
        mainScope.launch {
            try {
                val specialOffers = dataManager.getSpecialOffers()
                if (specialOffers.isEmpty())
                    view?.hideSpecialOffers()
                else {
                    view?.showSpecialOffers(specialOffers)
                }
            } catch (e: Exception) {
                view?.hideSpecialOffers()
            }
        }
    }
}