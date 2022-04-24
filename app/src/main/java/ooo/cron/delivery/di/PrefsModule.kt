package ooo.cron.delivery.di

import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import ooo.cron.delivery.data.local.PreferenceStorage
import ooo.cron.delivery.data.local.SharedPreferenceStorage

@Module
abstract class PrefsModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceStorage(storage: SharedPreferenceStorage): PreferenceStorage
}