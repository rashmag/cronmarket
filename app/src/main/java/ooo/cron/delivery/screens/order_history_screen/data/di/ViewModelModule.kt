package ooo.cron.delivery.screens.order_history_screen.data.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Named
import ooo.cron.delivery.screens.order_history_detail_screen.data.di.OrderHistoryDetailViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    @Named("List")
    abstract fun bindOrderHistoryViewModelFactory(factory: OrderHistoryViewModelFactory): ViewModelProvider.Factory

    @Binds
    @Named("Detail")
    abstract fun bindOrderHistoryDetailViewModelFactory(factory: OrderHistoryDetailViewModelFactory) : ViewModelProvider.Factory
}