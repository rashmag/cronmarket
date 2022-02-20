package ooo.cron.delivery.screens.order_history_detail_screen.data.repository

import javax.inject.Inject
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.OrderHistoryDetailDish
import ooo.cron.delivery.data.network.models.OrderHistoryDetailNetModel
import ooo.cron.delivery.screens.order_history_detail_screen.domain.repository.OrderHistoryDetailRepository
import retrofit2.Response

class OrderHistoryDetailRepositoryImpl @Inject constructor(
    private val api: RestService
) : OrderHistoryDetailRepository {

    override suspend fun getOrderHistoryDetail(token: String, orderId: String): Response<OrderHistoryDetailNetModel> {
        return api.getOrderHistoryDetail(token, orderId)
    }
}