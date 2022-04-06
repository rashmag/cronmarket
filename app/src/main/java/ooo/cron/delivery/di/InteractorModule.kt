package ooo.cron.delivery.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import ooo.cron.delivery.data.PrefsRepository
import ooo.cron.delivery.data.network.AuthInteractor
import javax.inject.Singleton

/**
 * Created by Maya Nasrueva on 28.03.2021
 * */

@Module
class InteractorModule {

    @Provides
    @Reusable
    fun provideAuthInteractor(
        prefsRepository: PrefsRepository
    ): AuthInteractor =
        AuthInteractor(prefsRepository)
}