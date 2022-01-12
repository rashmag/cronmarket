package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.request.OrderReq
import javax.inject.Inject

class OrderRestRepository @Inject constructor(
    private val restService: RestService
) {
    suspend fun getBasket(basketId: String) = restService.getSimpleBasket(basketId)

    suspend fun sendOrder(token:String, basketId: String, phone: String?, comment: String, deliveryCityId: String?, paymentMethod:Int) =
        restService.sendOrder(token, OrderReq(
            basketId,
            phone,
            comment,
            deliveryCityId,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ))
}