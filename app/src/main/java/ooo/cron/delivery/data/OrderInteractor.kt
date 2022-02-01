package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.request.BasketClearReq
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
        prefsRepo.readBasket()?.let {
            prefsRepo.readToken()?.accessToken?.let { it1 ->
                restRepo.sendOrder(
                    it1,
                    it.id,
                    prefsRepo.readUserPhone(),
                    comment,
                    prefsRepo.readDeliveryCityId(),
                    paymentMethod
                )
            }
        }
    }

    suspend fun clearBasket(basketClearReq: BasketClearReq) =
        restRepo.clearBasket(basketClearReq)

    fun getBasketClearReq(basket: Basket) =
        restRepo.getBasketClearReq(basket)

    fun getBasket(): Basket? =
        prefsRepo.readBasket()

    fun getPhone(): String? =
        prefsRepo.readUserPhone()
}