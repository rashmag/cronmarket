package ooo.cron.delivery.screens.splash_screen

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ramazan Gadzhikadiev on 10.06.2021.
 */

@Module
interface SplashScreenModule {

    @Binds
    fun bindPresenter(presenter: SplashScreenPresenter):
            SplashScreenContract.Presenter

    companion object {
        @JvmStatic
        @Provides
        fun provideCoroutineScope() =
            CoroutineScope(Dispatchers.Main)
    }
}