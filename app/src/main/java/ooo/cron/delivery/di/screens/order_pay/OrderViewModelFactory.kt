package ooo.cron.delivery.di.screens.order_pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ooo.cron.delivery.data.OrderInteractor
import ooo.cron.delivery.screens.pay_dialog_screen.OrderViewModel

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderViewModelFactory @AssistedInject constructor(
    private val interactor: OrderInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderViewModel::class.java)
        return OrderViewModel(interactor) as T
    }

    @AssistedFactory
    @OrderScope
    interface Factory {
        fun create(): OrderViewModelFactory
    }
}