package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketPersonsReq
import ooo.cron.delivery.data.network.models.RemoveBasketItemReq
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.data.network.request.OrderReq
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 17.12.2021
 * */

class RestRepository @Inject constructor(
    private val restService: RestService
) {
    suspend fun getBasket(basketId: String): Result<Basket> {
        val response = restService.getBasket(basketId)
        val body = response.body()
        return if (body != null) {
            Result.success(body)
        } else
            Result.failure(Exception())
    }

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

    suspend fun increaseProductInBasket(editor: BasketEditorReq): Basket =
        restService.increaseProductInBasket(editor)

    suspend fun decreaseProductInBasket(editor: BasketEditorReq):Basket =
        restService.decreaseProductInBasket(editor)

    suspend fun editPersonsQuantity(persons: BasketPersonsReq): Basket =
        restService.editPersonsCount(persons)

    suspend fun clearBasket(basketClearReg: BasketClearReq): Basket =
        restService.clearBasket(basketClearReg)

    suspend fun removeBasketItem(removeBasketItem: RemoveBasketItemReq): Basket =
        restService.removeProduct(removeBasketItem)
}