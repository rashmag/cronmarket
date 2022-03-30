package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.utils.Result
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 09.01.2022
 * */

class OrderInteractor @Inject constructor(
    private val restRepo: RestRepository,
    private val prefsRepo: PrefsRepository
) {
    //suspend fun getBasket() = restRepo.getBasket(prefsRepo.readBasket().id)

    suspend fun sendOrder(paymentMethod: Int, comment: String ) {
        val basket = prefsRepo.readBasket()
        val token = prefsRepo.readToken()
        if (basket == null || token == null) return
        restRepo.sendOrder(
            token.accessToken,
            basket.id,
            prefsRepo.readUserPhone(),
            comment,
            prefsRepo.readDeliveryCity().id,
            address = null,
            entrance = null,
            floor = null,
            flat = null,
            deliveryAtTime = null,
            saveAddress = null,
            discount = null,
            paymentMethod
        )
    }

    suspend fun clearBasket(basketClearReq: BasketClearReq): Result<Basket> {
        return restRepo.clearBasket(basketClearReq)
    }


    fun getBasketClearReq(basket: Basket): BasketClearReq {
        return restRepo.getBasketClearReq(basket)
    }

    fun getBasket(): Basket? {
        return prefsRepo.readBasket()
    }

    fun getPhone(): String? {
        return prefsRepo.readUserPhone()
    }

    fun getDeliveryCityId(): String {
        return prefsRepo.readDeliveryCity().id
    }
}