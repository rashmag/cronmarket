package ooo.cron.delivery.screens.order_history_detail_screen.domain.repository

import ooo.cron.delivery.data.network.models.OrderHistoryDetailDish
import ooo.cron.delivery.data.network.models.OrderHistoryDetailNetModel
import retrofit2.Response

interface OrderHistoryDetailRepository {

    suspend fun getOrderHistoryDetail(token: String, orderId: String): Response<OrderHistoryDetailNetModel>
}