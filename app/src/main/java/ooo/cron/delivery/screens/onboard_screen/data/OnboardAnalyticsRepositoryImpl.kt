package ooo.cron.delivery.screens.onboard_screen.data

import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.screens.onboard_screen.domain.OnboardAnalyticsRepository
import javax.inject.Inject

class OnboardAnalyticsRepositoryImpl @Inject constructor(
    private val analytics: BaseAnalytics
) : OnboardAnalyticsRepository {
    override suspend fun sendMessageInAnalutics(message: String) {
        analytics.trackOpenOnboardScreen(message)
    }
}