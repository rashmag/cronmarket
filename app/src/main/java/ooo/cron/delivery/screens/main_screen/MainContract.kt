package ooo.cron.delivery.screens.main_screen

import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.data.network.models.PartnersInfoRes
import ooo.cron.delivery.data.network.models.Promotion
import ooo.cron.delivery.data.network.models.User
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */
interface MainContract {

    interface View : MvpView {
        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()
        fun showSavedAddress(address: String)
        fun showMarketCategories(categories: List<MarketCategory>)
        fun showAuthorizedUser(username: String)
        fun showUnauthorizedUser()
        fun shouldLastBasketSessionBeVisible(boolean: Boolean)
        fun showContinueLastSession()
        fun hideContinueLastSession()
        fun hideContinueLastSessionMainMenu()
        fun removeMarketCategoriesProgress()
        fun showNotAuthorizedMessage()
        fun showLogOutDialog()
        fun startMarketCategoryFragment(category: MarketCategory?)
        fun startFavoritePartnersFragment()
        fun startOrdersHistoryFragment()
        fun startContactsFragment()
        fun reopenMainScreen()
        fun navigateFirstAddressSelection()
        fun navigateAddressSelection()
        fun setPartnerClickedBaner(partnersInfoRes: PartnersInfoRes?)
        fun navigateLoginActivity()
        fun navigatePartnerScreen(partnerId: String)
        fun startAboutServiceFragment()
        fun startVacanciesFragment()
        fun showSpecialOffers(promotions: List<Promotion>)
        fun hideSpecialOffers()
        fun showPartnerName(parnerName: String?)
        fun showBasketAmount(basketAmount: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun onCreateScreen()
        fun onResumeView(isFromPartnerScreen: Boolean)
        fun onMarketCategoryClicked(category: MarketCategory)
        fun onClickAddress()
        fun onProfileClick()
        fun onPartnerClickedBaner(partnerId: String)
        fun onCheckEmptyBasket()
        fun onLogInLogOutClick()
        fun onLogOutApplied()
        fun continueLastSessionCLick()
        fun getMarketCategory(): MarketCategory?
        fun onStartMarketCategory()
        fun getUserLoggedStatus(): Boolean
    }
}