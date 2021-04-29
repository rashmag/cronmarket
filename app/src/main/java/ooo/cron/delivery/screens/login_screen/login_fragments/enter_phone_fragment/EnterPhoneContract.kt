package ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/*
 * Created by Muhammad on 28.04.2021
 */



interface EnterPhoneContract {

    interface View : MvpView {
        fun getPhone(): String
        fun showError(message:String)
        fun startNextScreen()
    }


    interface Presenter : MvpPresenter<View> {
        fun sendPhone()
    }
}