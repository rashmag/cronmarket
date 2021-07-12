package ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment

import ooo.cron.delivery.data.network.models.RefreshableToken
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/*
 * Created by Muhammad on 29.04.2021
 */



interface ConfirmPhoneContract {

    interface View: MvpView {
        fun getCode(): String
        fun showNextScreen()
        fun showError(message: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun sendConfirmCode()
        fun sendPhone()
        fun getUserPhone(): String?
    }
}