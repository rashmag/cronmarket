package ooo.cron.delivery.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ooo.cron.delivery.di.screens.onboard.OnboardViewModelFactory
import javax.inject.Named
import ooo.cron.delivery.di.screens.order_history.OrderHistoryViewModelFactory
import ooo.cron.delivery.di.screens.order_history_detail.OrderHistoryDetailViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    @Named("List")
    abstract fun bindOrderHistoryViewModelFactory(factory: OrderHistoryViewModelFactory): ViewModelProvider.Factory

    @Binds
    @Named("Detail")
    abstract fun bindOrderHistoryDetailViewModelFactory(factory: OrderHistoryDetailViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @Named("Onboard")
    abstract fun bindOnboardViewModelFactory(factory: OnboardViewModelFactory) : ViewModelProvider.Factory
}