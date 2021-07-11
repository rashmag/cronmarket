package ooo.cron.delivery.screens.main_screen

import ooo.cron.delivery.data.network.models.MarketCategory
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
        fun selectMarketCategory(position: Int)
        fun showAuthorizedUser(username: String)
        fun showUnauthorizedUser()
        fun shouldLastBasketSessionBeVisible(boolean: Boolean)
        fun showContinueLastSession()
        fun hideContinueLastSession()
        fun removeMarketCategoriesProgress()
        fun showNotAuthorizedMessage()
        fun showLogOutDialog()
        fun startMarketCategoryFragment(category: MarketCategory)
        fun startContactsFragment()
        fun reopenMainScreen()
        fun navigateFirstAddressSelection()
        fun navigateAddressSelection()
        fun navigateLoginActivity()
        fun navigatePartnerScreen(partnerId: String)
        fun startAboutServiceFragment()
        fun startVacanciesFragment()
        fun showSpecialOffers(promotions: List<Promotion>)
        fun hideSpecialOffers()
    }

    interface Presenter : MvpPresenter<View> {
        fun onCreateView()
        fun onResumeView()
        fun onTabSelected(position:Int)
        fun onClickAddress()
        fun onProfileClick()
        fun onLogInLogOutClick()
        fun onLogOutApplied()
        fun continueLastSessionCLick()
        fun getMarketCategory(): MarketCategory
        fun onStartMarketCategory()
    }
}