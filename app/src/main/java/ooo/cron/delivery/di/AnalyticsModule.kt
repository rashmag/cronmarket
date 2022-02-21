package ooo.cron.delivery.di

import android.content.Context
import com.yandex.metrica.IReporter
import com.yandex.metrica.YandexMetrica
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import ooo.cron.delivery.App
import ooo.cron.delivery.BuildConfig
import ooo.cron.delivery.analytics.AnalyticsTracker
import ooo.cron.delivery.analytics.AnalyticsTrackers
import ooo.cron.delivery.analytics.LogAnalyticsTracker
import ooo.cron.delivery.analytics.YandexMetricaAnalyticsTracker

@Module
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideYandexMetricaReporter(
        context: Context
    ): IReporter {
        return YandexMetrica.getReporter(context, App.YANDEX_API_KEY)
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideYandexMetricaAnalyticsTracker(reporter: IReporter): AnalyticsTracker {
        return YandexMetricaAnalyticsTracker(reporter)
    }

    @Provides
    @Singleton
    fun segmentTrackers(analyticsImpl: YandexMetricaAnalyticsTracker): AnalyticsTrackers = when {
        BuildConfig.DEBUG -> listOf(LogAnalyticsTracker())
        else -> listOf(analyticsImpl)
    }
}