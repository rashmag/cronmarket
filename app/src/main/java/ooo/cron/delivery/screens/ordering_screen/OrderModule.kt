package ooo.cron.delivery.screens.ordering_screen

import android.view.LayoutInflater
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.databinding.ActivityOrderingBinding

/*
 * Created by Muhammad on 19.05.2021
 */


@Module
interface OrderModule {
    @Binds
    @OrderScope
    fun bindPresenter(presenter: OrderPresenter): OrderContract.Presenter

    @Module
    companion object {
        @Provides
        fun provideBinding(inflater: LayoutInflater) =
            ActivityOrderingBinding.inflate(inflater)

        @Provides
        fun provideOrderingScope() = CoroutineScope(Dispatchers.Main)
    }
}