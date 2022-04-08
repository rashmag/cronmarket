package ooo.cron.delivery.di.screens.order_history

import dagger.Binds
import dagger.Module
import ooo.cron.delivery.screens.onboard_screen.data.OnboardAnalyticsRepImpl
import ooo.cron.delivery.screens.onboard_screen.domain.OnboardAnalyticsRep
import javax.inject.Singleton
import ooo.cron.delivery.screens.order_history_detail_screen.data.repository.OrderHistoryDetailRepositoryImpl
import ooo.cron.delivery.screens.order_history_detail_screen.domain.repository.OrderHistoryDetailRepository
import ooo.cron.delivery.screens.order_history_screen.data.repository.OrderHistoryRepositoryImpl
import ooo.cron.delivery.screens.order_history_screen.domain.repository.OrderHistoryRepository

@Module
abstract class RepositoriesModule {

    @Binds
    abstract fun bindOrderHistoryRepository(orderHistoryRepositoryImpl: OrderHistoryRepositoryImpl): OrderHistoryRepository

    @Binds
    abstract fun bindOrderHistoryDetailRepository(orderHistoryDetailRepositoryImpl: OrderHistoryDetailRepositoryImpl): OrderHistoryDetailRepository

    @Binds
    abstract fun bindOnboardRepository(onboardAnalyticsRepImpl: OnboardAnalyticsRepImpl): OnboardAnalyticsRep
}