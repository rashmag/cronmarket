package ooo.cron.delivery.screens.order_history_screen.domain.usecases

import javax.inject.Inject
import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import ooo.cron.delivery.screens.order_history_screen.domain.repository.OrderHistoryRepository
import retrofit2.Response

class LoadOrderHistoryUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {

    suspend operator fun invoke(token: String): Response<List<OrderHistoryNetModel>>? {
        return orderHistoryRepository.loadOrderHistory(token)
    }
}