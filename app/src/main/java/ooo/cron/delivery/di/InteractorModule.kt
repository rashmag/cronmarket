package ooo.cron.delivery.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton
import ooo.cron.delivery.data.BasketInteractor
import ooo.cron.delivery.data.PrefsRepository
import ooo.cron.delivery.data.RestRepository
import ooo.cron.delivery.data.network.AuthInteractor

/**
* Created by Maya Nasrueva on 28.03.2021
* */

@Module
class InteractorModule {

    @Provides
    @Singleton
    fun provideBasketInteractor(
        restRepository: RestRepository,
        prefsRepository: PrefsRepository
    ): BasketInteractor =
        BasketInteractor(restRepository, prefsRepository)

    @Reusable
    fun provideAuthInteractor(
        prefsRepository: PrefsRepository
    ): AuthInteractor =
        AuthInteractor(prefsRepository)
}