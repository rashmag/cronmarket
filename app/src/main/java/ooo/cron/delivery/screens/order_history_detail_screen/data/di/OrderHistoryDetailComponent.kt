package ooo.cron.delivery.screens.order_history_detail_screen.data.di

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.screens.order_history_detail_screen.presentation.OrderHistoryDetailFragment
import ooo.cron.delivery.screens.order_history_screen.data.di.RepositoriesModule
import ooo.cron.delivery.screens.order_history_screen.data.di.ViewModelModule

@OrderHistoryDetailScope
@Subcomponent(modules = [ViewModelModule::class, RepositoriesModule::class])
interface OrderHistoryDetailComponent {

    fun inject(fragment: OrderHistoryDetailFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): OrderHistoryDetailComponent
    }
}