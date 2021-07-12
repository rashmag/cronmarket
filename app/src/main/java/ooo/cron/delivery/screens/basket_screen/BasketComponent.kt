package ooo.cron.delivery.screens.basket_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */

@Subcomponent(modules = [BasketModule::class])
@BasketScope
interface BasketComponent {

    fun inject(activity: BasketActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun inflater(inflater: LayoutInflater): Builder
        fun build(): BasketComponent
    }

}