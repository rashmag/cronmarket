package ooo.cron.delivery.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import ooo.cron.delivery.data.BasketInteractor
import ooo.cron.delivery.data.OrderInteractor
import ooo.cron.delivery.data.PrefsRepository
import ooo.cron.delivery.data.RestRepository
import javax.inject.Singleton

/**
* Created by Maya Nasrueva on 28.03.2021
* */

@Module
class InteractorModule {

    @Provides
    @Singleton
    fun provideOrderInteractor(
        restRepository: RestRepository,
        prefsRepository: PrefsRepository
    ): OrderInteractor =
        OrderInteractor(restRepository, prefsRepository)

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