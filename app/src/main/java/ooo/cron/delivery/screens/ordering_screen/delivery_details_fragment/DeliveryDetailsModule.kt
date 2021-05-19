package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import android.view.LayoutInflater
import android.view.View
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.databinding.FragmentDeliveryDetailsBinding

/*
 * Created by Muhammad on 19.05.2021
 */


@Module
interface DeliveryDetailsModule {
    @Binds
    @DeliveryDetailsScope
    fun bindPresenter(presenter: DeliveryDetailsPresenter): DeliveryDetailsContract.Presenter

    @Module
    companion object {
        @Provides
        fun provideBinding(inflater: LayoutInflater) =
            FragmentDeliveryDetailsBinding.inflate(inflater)

        @Provides
        fun provideDeliveryDetailsScope() = CoroutineScope(Dispatchers.Main)
    }
}