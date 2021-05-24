package ooo.cron.delivery.screens.ordering_screen

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/*
 * Created by Muhammad on 19.05.2021
 */



interface OrderContract {

    interface View : MvpView {
        fun getBasketId(): String
        fun getPhone(): String
        fun getAddress(address: String)
    }

    interface Presenter : MvpPresenter<View> {

    }
}