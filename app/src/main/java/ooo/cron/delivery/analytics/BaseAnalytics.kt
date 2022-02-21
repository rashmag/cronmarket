package ooo.cron.delivery.analytics

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseAnalytics @Inject constructor(
    private val analyticsTrackers: AnalyticsTrackers
): Analytics {

    private fun trackEvent(event: String, params: Map<String, Any>?) {
        analyticsTrackers.forEach {
            it.trackEvent(event, params)
        }
    }

    override fun trackOpenMainScreen(phoneNumber: String) {
        trackEvent(
            "openMain",
            mapOf(
                "phone_number" to phoneNumber
            )
        )
    }
}