package ooo.cron.delivery

import android.app.Application
import ooo.cron.delivery.di.AppComponent
import ooo.cron.delivery.di.DaggerAppComponent

/**
 * Created by Ramazan Gadzhikadiev on 07.04.2021.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().bindsApplication(this).build()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE: Int = 83

        lateinit var appComponent: AppComponent
    }
}