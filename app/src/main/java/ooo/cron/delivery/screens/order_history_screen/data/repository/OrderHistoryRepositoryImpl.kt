package ooo.cron.delivery.screens.order_history_screen.data.repository

import javax.inject.Inject
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import ooo.cron.delivery.screens.order_history_screen.domain.repository.OrderHistoryRepository
import retrofit2.Response

class OrderHistoryRepositoryImpl @Inject constructor(
    private val api: RestService
) : OrderHistoryRepository {

    override suspend fun loadOrderHistory(): Response<List<OrderHistoryNetModel>> {
        return api.getOrdersHistory()
    }
}