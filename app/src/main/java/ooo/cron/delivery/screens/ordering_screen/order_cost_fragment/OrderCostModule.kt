package ooo.cron.delivery.screens.ordering_screen.order_cost_fragment

import android.view.LayoutInflater
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.databinding.FragmentOrderCostBinding

/*
 * Created by Muhammad on 19.05.2021
 */


@Module
interface OrderCostModule {
    @Binds
    @OrderCostScope
    fun bindPresenter(presenter: OrderCostPresenter): OrderCostContract.Presenter

    @Module
    companion object {
        @Provides
        fun provideBinding(inflater: LayoutInflater) =
            FragmentOrderCostBinding.inflate(inflater)

        @Provides
        fun provideDeliveryDetailsScope() = CoroutineScope(Dispatchers.Main)
    }
}