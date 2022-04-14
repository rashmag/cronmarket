package ooo.cron.delivery.di.screens.order_history_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Named
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.order_history_detail_screen.domain.usecases.LoadOrderHistoryDetailUseCase
import ooo.cron.delivery.screens.order_history_detail_screen.presentation.OrderHistoryDetailViewModel

class OrderHistoryDetailViewModelFactory @Inject constructor(
    private val loadOrderHistoryDetailUseCase: LoadOrderHistoryDetailUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderHistoryDetailViewModel::class.java)
        return OrderHistoryDetailViewModel( loadOrderHistoryDetailUseCase) as T
    }

    @OrderHistoryDetailScope
    @Named("Detail")
    interface Factory {
        fun create(): OrderHistoryDetailViewModelFactory
    }
}