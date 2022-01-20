package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketPersonsReq
import ooo.cron.delivery.data.network.models.RemoveBasketItemReq
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.01.2022
 * */

class BasketInteractor @Inject constructor(
    private val restRepository: RestRepository,
    private val prefsRepository: PrefsRepository
) {
    suspend fun getBasket(basketId: String) =
        restRepository.getBasket(basketId)

    fun getBasketId() =
        prefsRepository.readBasket().id

    fun getToken() =
        prefsRepository.readToken()

    fun writeBasket(basket: Basket) =
        prefsRepository.writeBasket(basket)

    suspend fun increaseProductInBasket(editor: BasketEditorReq) =
        restRepository.increaseProductInBasket(editor)

    suspend fun decreaseProductInBasket(editor: BasketEditorReq) =
        restRepository.decreaseProductInBasket(editor)

    suspend fun editPersonsQuantity(persons: BasketPersonsReq) =
        restRepository.editPersonsQuantity(persons)

    suspend fun clearBasket(basketClearReq: BasketClearReq) =
        restRepository.clearBasket(basketClearReq)

    suspend fun removeBasketItem(removeBasketItem: RemoveBasketItemReq) =
        restRepository.removeBasketItem(removeBasketItem)
}