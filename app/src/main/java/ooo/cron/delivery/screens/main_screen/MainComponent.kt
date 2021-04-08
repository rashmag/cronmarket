package ooo.cron.delivery.screens.main_screen

import dagger.Subcomponent

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@MainScope
@Subcomponent (modules = [MainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build(): MainComponent
    }
}