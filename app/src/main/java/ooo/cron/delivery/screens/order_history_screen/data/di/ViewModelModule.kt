package ooo.cron.delivery.screens.order_history_screen.data.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ooo.cron.delivery.screens.order_history_screen.data.di.OrderHistoryViewModelFactory

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindOrderHistoryViewModelFactory(factory: OrderHistoryViewModelFactory): ViewModelProvider.Factory
}