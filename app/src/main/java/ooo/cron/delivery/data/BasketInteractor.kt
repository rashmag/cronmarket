package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.models.*
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
        prefsRepository.readBasketId()

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

    fun getBasketPersonReq(basket: Basket, quantity: Int) =
        restRepository.getBasketPersonReq(basket, quantity)

    fun getBasketClearReq(basket: Basket) =
        restRepository.getBasketClearReq(basket)

    fun getRemoveBasketItemReq(product: BasketDish?, basket: Basket) =
        restRepository.getRemoveBasketItemReq(product, basket)

    fun getBasketEditorReq(basket: Basket, addingProduct: BasketDish) =
        restRepository.getBasketEditorReq(basket, addingProduct)
}