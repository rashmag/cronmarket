package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.databinding.ActivityPartnersBinding

/*
 * Created by Muhammad on 05.05.2021
 */

@Module
interface PartnersModule {
    @Binds
    @PartnersScope
    fun bindPresenter(presenter: PartnersPresenter): PartnersContract.Presenter

    @Module
    companion object {
        @Provides
        fun provideBinding(inflater: LayoutInflater) =
            ActivityPartnersBinding.inflate(inflater)

        @Provides
        fun providePartnersScope() = CoroutineScope(Dispatchers.Main)
    }
}