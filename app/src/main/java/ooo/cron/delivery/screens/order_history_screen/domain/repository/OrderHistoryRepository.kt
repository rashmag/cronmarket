package ooo.cron.delivery.screens.order_history_screen.domain.repository

import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import retrofit2.Response

interface OrderHistoryRepository {

    suspend fun loadOrderHistory(token: String): Response<List<OrderHistoryNetModel>>?
}