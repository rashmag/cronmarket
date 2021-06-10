package ooo.cron.delivery.screens.splash_screen

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 10.06.2021.
 */

interface SplashScreenContract {
    interface View: MvpView {
        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()

        fun showUpdateVersionDialog()

        fun navigateFirstAddressScreen()
        fun navigateMainScreen()
        fun navigateGooglePlay()

        fun finish()
    }

    interface Presenter: MvpPresenter<View> {
        fun onStartView()
        fun updateAccepted()
        fun updateDeclined()
    }
}