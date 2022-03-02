package ooo.cron.delivery.analytics

import android.util.Log

class LogAnalyticsTracker : AnalyticsTracker {

    private val TAG = "LogTracker"

    override fun trackEvent(event: String, params: Map<String, Any>?) {
        val entries = params?.entries
        Log.d(
            TAG,
            buildString {
                append("\"${event}\"")
                if (entries.isNullOrEmpty().not()) {
                    append(" with")
                }
                entries?.forEach { entry ->
                    append(" ${entry.key} = `${entry.value}`,")
                }
                removeSuffix(",")
            }
        )
    }
}