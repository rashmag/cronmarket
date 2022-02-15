package ooo.cron.delivery.screens.order_history_detail_screen.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Named
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.order_history_detail_screen.domain.usecases.LoadOrderHistoryDetailUseCase
import ooo.cron.delivery.screens.order_history_detail_screen.presentation.OrderHistoryDetailViewModel
import ooo.cron.delivery.screens.order_history_screen.domain.usecases.LoadOrderHistoryUseCase
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryViewModel

class OrderHistoryDetailViewModelFactory @Inject constructor(
    private val dataManager: DataManager,
    private val loadOrderHistoryDetailUseCase: LoadOrderHistoryDetailUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderHistoryDetailViewModel::class.java)
        return OrderHistoryDetailViewModel(dataManager, loadOrderHistoryDetailUseCase) as T
    }

    @OrderHistoryDetailScope
    @Named("Detail")
    interface Factory {
        fun create(): OrderHistoryDetailViewModelFactory
    }
}