package ooo.cron.delivery.screens.onboard_screen

import android.util.Log
import ooo.cron.delivery.R
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import javax.inject.Inject

class OnboardPresenter @Inject constructor(
    private val analytics: BaseAnalytics
) : BaseMvpPresenter<OnboardContact.View>(), OnboardContact.Presenter {
    override fun sendMessageOnboardCompleted() {
        analytics.onboardComponentBuilder(R.string.completed_onboard.toString())
    }
}