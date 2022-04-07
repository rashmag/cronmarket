package ooo.cron.delivery.screens.onboard_screen

import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

interface OnboardContact {

    interface View : MvpView{
    }

    interface Presenter : MvpPresenter<View> {
        fun sendMessageOnboardCompleted()
    }
}