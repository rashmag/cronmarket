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

    suspend fun sendOrder(
        paymentMethod: Int,
        comment: String,
        address: String,
        deliveryAtTime: String
    ) {
        val basket = prefsRepo.readBasket()
        if (basket == null) return
        restRepo.sendOrder(
            basket.id,
            prefsRepo.readUserPhone(),
            comment,
            prefsRepo.readDeliveryCity().id,
            address = address,
            entrance = null,
            floor = null,
            flat = null,
            deliveryAtTime = deliveryAtTime,
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

    fun readAddress(): String{
        return prefsRepo.readBuildingAddress()
    }

    fun getPartnerOpenHours(): Int{
        return prefsRepo.readPartnerOpenHours()
    }

    fun getPartnerCloseHours(): Int{
        return prefsRepo.readPartnerCloseHours()
    }
}