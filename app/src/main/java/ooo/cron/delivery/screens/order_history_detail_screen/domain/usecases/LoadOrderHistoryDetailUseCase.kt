package ooo.cron.delivery.screens.order_history_detail_screen.domain.usecases

import javax.inject.Inject
import ooo.cron.delivery.data.network.models.OrderHistoryDetailDish
import ooo.cron.delivery.data.network.models.OrderHistoryDetailNetModel
import ooo.cron.delivery.screens.order_history_detail_screen.domain.repository.OrderHistoryDetailRepository
import retrofit2.Response

class LoadOrderHistoryDetailUseCase @Inject constructor(
    private val orderHistoryDetailRepository: OrderHistoryDetailRepository
) {

    suspend operator fun invoke( orderId: String): Response<OrderHistoryDetailNetModel>{
        return orderHistoryDetailRepository.getOrderHistoryDetail(orderId)
    }
}