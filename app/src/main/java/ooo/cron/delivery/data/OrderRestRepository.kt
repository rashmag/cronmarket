package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.request.OrderReq
import javax.inject.Inject

class OrderRestRepository @Inject constructor(
    private val restService: RestService
) {
    suspend fun getBasket(basketId: String) = restService.getSimpleBasket(basketId)

    suspend fun sendOrder(token:String, orderRequest: OrderReq) =
        restService.sendOrder(token, orderRequest)
}