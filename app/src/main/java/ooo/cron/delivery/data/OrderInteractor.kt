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
    private val prefsRepo: PrefsRepository,
    private val prefs: PreferenceStorage
) {

    suspend fun sendOrder(
        basketId:String,
        paymentMethod: Int,
        comment: String,
        address: String,
        deliverAtTime: String?
    ): Response<ResponseBody> {
            return restRepo.sendOrder(
                basketId = basketId,
                prefsRepo.readUserPhone(),
                comment,
                prefsRepo.readDeliveryCity().id,
                address = address,
                entrance = "",
                floor = "",
                flat = "",
                deliverAtTime = deliverAtTime,
                saveAddress = true,
                discount = 0,
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

    fun getPartnerOpenTime(): LocalTime? {
        return prefs.partnerOpenTime?.parseTime()
    }

    fun getPartnerCloseTime(): LocalTime? {
        return prefs.partnerCloseTime?.parseTime()
    }
}