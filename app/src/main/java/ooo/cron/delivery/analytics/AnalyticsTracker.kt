package ooo.cron.delivery.analytics

interface AnalyticsTracker {
    fun trackEvent(event: String, params: Map<String, Any>? = null)
}