package ooo.cron.delivery.screens.pay_dialog_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.OrderPrefsRepository
import ooo.cron.delivery.data.OrderRestRepository

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderViewModelFactory @AssistedInject constructor(
    private val restRepo: OrderRestRepository,
    private val prefsRepo: OrderPrefsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderViewModel::class.java)
        return OrderViewModel(restRepo, prefsRepo) as T
    }

    @AssistedFactory
    @OrderScope
    interface Factory {

        fun create(): OrderViewModelFactory
    }
}