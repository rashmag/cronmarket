package ooo.cron.delivery.analytics

import com.yandex.metrica.IReporter
import javax.inject.Inject

class YandexMetricaAnalyticsTracker @Inject constructor(
    private val reporter: IReporter
) : AnalyticsTracker {

    init {
        reporter.setStatisticsSending(true)
    }

    override fun trackEvent(event: String, params: Map<String, Any>?) {
        reporter.reportEvent(event, params)
    }
}
