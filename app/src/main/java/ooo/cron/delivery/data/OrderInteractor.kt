package ooo.cron.delivery.data

import android.content.SharedPreferences
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.request.OrderReq
import javax.inject.Inject

class OrderInteractor @Inject constructor(
    private val restRepo: OrderRestRepository,
    private val prefsRepo: OrderPrefsRepository
) {
    //suspend fun getBasket() = restRepo.getBasket(prefsRepo.readBasket().id)

    suspend fun sendOrder(paymentMethod: Int, comment: String ) {
        restRepo.sendOrder(
            prefsRepo.readToken().accessToken,
            prefsRepo.readBasket().id,
            prefsRepo.readUserPhone(),
            comment,
            prefsRepo.readDeliveryCityId(),
            paymentMethod
        )
    }

    fun getBasket(): Basket =
        prefsRepo.readBasket()

    fun getPhone(): String? =
        prefsRepo.readUserPhone()
}