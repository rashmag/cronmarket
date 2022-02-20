package ooo.cron.delivery.di.screens.order_history

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.di.ViewModelModule
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryFragment

@OrderHistoryScope
@Subcomponent(modules = [ViewModelModule::class, RepositoriesModule::class])
interface OrderHistoryComponent {

    fun inject(fragment: OrderHistoryFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): OrderHistoryComponent
    }
}