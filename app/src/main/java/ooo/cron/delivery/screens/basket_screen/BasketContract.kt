package ooo.cron.delivery.screens.basket_screen

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
interface BasketContract {
    interface View : MvpView

    interface Presenter : MvpPresenter<View>
}