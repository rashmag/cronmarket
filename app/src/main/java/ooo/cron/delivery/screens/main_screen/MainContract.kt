package ooo.cron.delivery.screens.main_screen

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */
interface MainContract {

    interface View : MvpView

    interface Presenter : MvpPresenter<View>
}