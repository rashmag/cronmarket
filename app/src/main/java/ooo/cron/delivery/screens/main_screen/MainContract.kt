package ooo.cron.delivery.screens.main_screen

import ooo.cron.delivery.data.network.models.MarketCategory
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

        fun removeMarketCategoriesProgress()

        fun navigateFirstAddressSelection()
        fun navigateAddressSelection()
    }

    interface Presenter : MvpPresenter<View> {
        fun onStartView()

        fun onClickAddress()
    }
}