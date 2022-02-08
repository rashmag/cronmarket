package ooo.cron.delivery.screens.order_history_screen.data.di

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryFragment
import ooo.cron.delivery.screens.partners_screen.PartnersModule
import ooo.cron.delivery.screens.partners_screen.PartnersScope

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