package ooo.cron.delivery.screens.splash_screen

import dagger.Subcomponent

/**
 * Created by Ramazan Gadzhikadiev on 10.06.2021.
 */

@Subcomponent(modules = [SplashScreenModule::class])
interface SplashScreenComponent {

    fun inject(view: SplashScreenActivity)

    @Subcomponent.Builder
    interface Builder {

        fun build(): SplashScreenComponent
    }
}
