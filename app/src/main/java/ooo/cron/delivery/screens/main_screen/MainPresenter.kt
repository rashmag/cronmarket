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
import ooo.cron.delivery.analytics.BaseAnalytics

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@MainScope
class MainPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
    private val analytics: BaseAnalytics
) :
    BaseMvpPresenter<MainContract.View>(), MainContract.Presenter {

    private var currentChosenCity: String? = null
    private var marketCategories: List<MarketCategory>? = null
    private var user: UserResponse? = null
    private var basketPartnerId: String = DataManager.EMPTY_UUID

    override fun detachView() {
        super.detachView()
        mainScope.cancel()
    }

    override fun onCreateScreen() {
        analytics.trackOpenMainScreen(dataManager.readUserPhone().toString())
    }
    override fun onCheckEmptyBasket() {
        val basketId = dataManager.readUserBasketId()
        if (basketId == DataManager.EMPTY_UUID)
            view?.hideContinueLastSessionMainMenu()
    }

    override fun onResumeView(isFromPartnerScreen: Boolean) {
        mainScope.launch {
            defineAddress()
            if (currentChosenCity != dataManager.readChosenCity()?.id) {
                currentChosenCity = dataManager.readChosenCity()?.id
                currentChosenCity?.let { loadMarketCategories(it) }//todo нет обработки ошибки
                showMarketCategories(marketCategories!!.first())
            }

            dataManager.readToken()?.let { loadUser(it, isFromPartnerScreen) } //todo нет обработки ошибки

            val basketId = dataManager.readUserBasketId()

            if (basketId != DataManager.EMPTY_UUID) {

                // todo СДЕЛАТЬ ПО-ЧЕЛОВЕЧЕСКИ ПОСЛЕ РЕФАКТОРИНГА (ЕСЛИ ОН ВООБЩЕ ПЛАНИРУЕТСЯ) p.s. это пздц
                val basketResponse = dataManager.getBasket(basketId)

                if (basketResponse.isSuccessful) {

                    val basket = basketResponse.body()
                    basketPartnerId = basket?.partnerId ?: DataManager.EMPTY_UUID

                    val partnerInfoResponse = dataManager.getPartnersInfo(basketPartnerId)

                    if (basket?.amount?.toInt() == EMPTY_BASKET_COUNT) {
                        view?.shouldLastBasketSessionBeVisible(false)
                        view?.hideContinueLastSession()
                    } else{
                        if (dataManager.readCurrentCityId() == EMPTY_BECAUSE_FIRST_OPEN){
                            dataManager.readChosenCity()?.let { dataManager.writeCurrentCityId(it.id) } //todo нет обработки ошибки
                        }

                        if (dataManager.readChosenCity()?.id == dataManager.readCurrentCityId()){
                            view?.showContinueLastSession()
                        }else{
                            view?.hideContinueLastSession()
                        }
                        val partnerInfo = partnerInfoResponse.body()

                        view?.showPartnerName(partnerInfo?.name.toString())
                        view?.shouldLastBasketSessionBeVisible(true)
                        view?.showBasketAmount((basket?.amount?.toInt()).toString())
                    }

                    return@launch
                }
            }else
                view?.hideContinueLastSession()
        }
    }

    override fun getUserLoggedStatus(): Boolean {
        return dataManager.readToken()?.accessToken?.isNotEmpty() ?: false
    }

    override fun onMarketCategoryClicked(category: MarketCategory) {
        view?.startMarketCategoryFragment(category)
        dataManager.writeSelectedMarketCategory(category)
    }

    override fun onClickAddress() {
        view?.navigateFirstAddressSelection()
    }

    override fun onProfileClick() {
        if (user == null)
            view?.navigateLoginActivity()
    }

    override fun onPartnerClickedBaner(partnerId: String) {
        mainScope.launch {
            val partnerInfoResponse = dataManager.getPartnersInfo(partnerId)
            val partnerInfo = partnerInfoResponse.body()
            view?.setPartnerClickedBaner(partnerInfo)
        }
    }

    override fun onLogInLogOutClick() {
        if (user == null)
            return view?.navigateLoginActivity() ?: Unit

        view?.showLogOutDialog()
    }

    override fun onLogOutApplied() {
        val token = dataManager.readToken()
        if (token != null) {
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
    }

    override fun continueLastSessionCLick() {
        view?.navigatePartnerScreen(basketPartnerId)
    }

    private suspend fun defineAddress() {
        val address = dataManager.readBuildingAddress()
        view?.showSavedAddress(address.orEmpty())
    }

    private suspend fun loadMarketCategories(cityId: String) = withErrorsHandle(
        { dataManager.getMarketCategories(cityId).handleMarketCategories()},
        { view?.showConnectionErrorScreen() },
        { view?.showAnyErrorScreen() }
    )

    private suspend fun loadUser(token: RefreshableToken, isFromPartnerScreen: Boolean) {
        val response = dataManager.getUser()

        if (response.isSuccessful) {
            updateUser(response, isFromPartnerScreen)
            return
        }

        if (response.code() == 401 && token.accessToken.isNotEmpty() && token.refreshToken.isNotEmpty())
            return dataManager.refreshToken(token)
                .handleRefreshToken()
    }

    private fun Response<ResponseBody>.handleLogOut() {
        if (isSuccessful) {
            dataManager.removeToken()
            dataManager.removeUserBasketId()
            view?.reopenMainScreen()
        } else {
            view?.showAnyErrorScreen()
        }
    }

    private suspend fun Response<RefreshableToken>.handleRefreshToken(isFromPartnerScreen: Boolean = false) {
        if (isSuccessful) {
            dataManager.writeToken(body()!!)

            val userResponse = dataManager.getUser()
            if (userResponse.isSuccessful)
                return updateUser(userResponse, isFromPartnerScreen)
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
        dataManager.writeUserBasketId(response.body()?.basket?.id ?: DataManager.EMPTY_UUID)

    private fun updateUser(response: Response<UserResponse>, isFromPartnerScreen: Boolean) {
        handleBasket(response)
        showAuthorizeUser(response)
        if (!isFromPartnerScreen)
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
            if (dataManager.readChosenCity()?.id == user?.user?.lastDeliveryCityId) {
                val lastBoughtMarketCategoryPosition =
                    marketCategories!!.indexOfFirst { it.id == user?.user?.lastMarketCategoryId }
                if (lastBoughtMarketCategoryPosition != -1)
                    dataManager.writeSelectedMarketCategory(marketCategories!![lastBoughtMarketCategoryPosition])
            }
        }
    }

    override fun getMarketCategory(): MarketCategory? {
        dataManager.writeSelectedMarketCategory(marketCategories!!.first())
        return dataManager.readSelectedMarketCategory()
    }

    override fun onStartMarketCategory() {
        loadSpecialOrders()
    }

    private fun loadSpecialOrders() {
        mainScope.launch {
            try {
                val chosenCity = dataManager.readChosenCityId()
                val selectedMarketCategory = dataManager.readSelectedMarketCategory()?.id
                var specialOffers: List<Promotion> = listOf()
                if (chosenCity != null && selectedMarketCategory != null)
                    specialOffers = dataManager.getSpecialOffers(chosenCity,selectedMarketCategory)
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

    private companion object{
        private const val EMPTY_BASKET_COUNT = 0

        private const val EMPTY_BECAUSE_FIRST_OPEN = ""
    }
}