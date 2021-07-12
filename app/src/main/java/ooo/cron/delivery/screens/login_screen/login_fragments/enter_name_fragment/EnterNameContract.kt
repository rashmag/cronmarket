package ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/*
 * Created by Muhammad on 29.04.2021
 */



interface EnterNameContract {

    interface View : MvpView {
        fun getUserName(): String
        fun showError(parseError: String)
        fun cancelLogin()
    }

    interface Presenter : MvpPresenter<View> {
        fun sentUserName()
    }
}