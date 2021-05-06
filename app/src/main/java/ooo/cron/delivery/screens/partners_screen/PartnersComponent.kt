package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/*
 * Created by Muhammad on 05.05.2021
 */

@PartnersScope
@Subcomponent (modules = [PartnersModule::class])
interface PartnersComponent {

    fun inject(activity: PartnersActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): PartnersComponent
    }
}