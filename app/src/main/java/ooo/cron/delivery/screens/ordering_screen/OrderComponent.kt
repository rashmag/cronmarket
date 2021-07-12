package ooo.cron.delivery.screens.ordering_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/*
 * Created by Muhammad on 19.05.2021
 */


@OrderScope
@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(orderingActivity: OrderingActivity)


    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun buildInstance(inflater: LayoutInflater): Builder

        fun build(): OrderComponent
    }
}