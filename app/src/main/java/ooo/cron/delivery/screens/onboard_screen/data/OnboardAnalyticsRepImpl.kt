package ooo.cron.delivery.screens.onboard_screen.data

import android.util.Log
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.screens.onboard_screen.domain.OnboardAnalyticsRep
import javax.inject.Inject

class OnboardAnalyticsRepImpl @Inject constructor(
    private val analytics: BaseAnalytics
) : OnboardAnalyticsRep {
    override suspend fun sendMessageInAnalutics(message: String) {
        analytics.trackOpenOnboardScreen(message)
    }
}