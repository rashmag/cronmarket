package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/*
 * Created by Muhammad on 19.05.2021
 */


@DeliveryDetailsScope
@Subcomponent(modules = [DeliveryDetailsModule::class])
interface DeliveryDetailsComponent {

    fun inject(deliveryDetailsFragment: DeliveryDetailsFragment)


    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun buildInstance(inflater: LayoutInflater): Builder

        fun build(): DeliveryDetailsComponent
    }
}