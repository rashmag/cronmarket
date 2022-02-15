package ooo.cron.delivery.screens.order_history_screen.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Named
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.order_history_screen.domain.usecases.LoadOrderHistoryUseCase
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryViewModel
import ooo.cron.delivery.screens.ordering_screen.OrderScope

class OrderHistoryViewModelFactory @Inject constructor(
    private val dataManager: DataManager,
    private val loadOrderHistoryUseCase: LoadOrderHistoryUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderHistoryViewModel::class.java)
        return OrderHistoryViewModel(dataManager, loadOrderHistoryUseCase) as T
    }

    @OrderHistoryScope
    @Named("List")
    interface Factory {
        fun create(): OrderHistoryViewModelFactory
    }
}