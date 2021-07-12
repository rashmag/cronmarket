package ooo.cron.delivery.screens.main_screen

import android.app.Activity
import android.view.LayoutInflater
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.databinding.ActivityMainBinding

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */
@Module
interface MainModule {
    @Binds
    @MainScope
    fun bindPresenter(presenter: MainPresenter): MainContract.Presenter

    @Module
    companion object {
        @Provides
        fun provideBinding(inflater: LayoutInflater) =
            ActivityMainBinding.inflate(inflater)

        @Provides
        fun provideMainScope() = CoroutineScope(Dispatchers.Main)
    }
}