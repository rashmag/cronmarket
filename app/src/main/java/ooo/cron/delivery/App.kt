package ooo.cron.delivery

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import ooo.cron.delivery.di.AppComponent
import ooo.cron.delivery.di.DaggerAppComponent

/**
 * Created by Ramazan Gadzhikadiev on 07.04.2021.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().bindsApplication(this).build()

        val config = YandexMetricaConfig.newConfigBuilder(YANDEX_API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE: Int = 83

        lateinit var appComponent: AppComponent

        const val YANDEX_API_KEY = "f04e185d-1f2e-4c14-b3db-7348c70e525b"
    }
}