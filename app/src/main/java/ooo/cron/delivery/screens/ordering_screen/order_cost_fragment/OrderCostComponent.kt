package ooo.cron.delivery.screens.ordering_screen.order_cost_fragment

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/*
 * Created by Muhammad on 19.05.2021
 */


@OrderCostScope
@Subcomponent(modules = [OrderCostModule::class])
interface OrderCostComponent {

    fun inject(orderCostFragment: OrderCostFragment)


    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun buildInstance(inflater: LayoutInflater): Builder

        fun build(): OrderCostComponent
    }
}