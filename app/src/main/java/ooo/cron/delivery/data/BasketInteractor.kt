package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.utils.Result
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.01.2022
 * */

class BasketInteractor @Inject constructor(
    private val restRepository: RestRepository,
    private val prefsRepository: PrefsRepository
) {
    suspend fun getBasket(basketId: String): Result<Basket> =
        restRepository.getBasket(basketId)

    fun getBasketId(): String? =
        prefsRepository.readBasketId()

    fun getToken(): RefreshableToken? =
        prefsRepository.readToken()

    fun writeBasket(basket: Basket) =
        prefsRepository.writeBasket(basket)

    suspend fun increaseProductInBasket(editor: BasketEditorReq): Result<Basket> =
        restRepository.increaseProductInBasket(editor)

    suspend fun decreaseProductInBasket(editor: BasketEditorReq): Result<Basket> =
        restRepository.decreaseProductInBasket(editor)

    suspend fun editPersonsQuantity(persons: BasketPersonsReq): Result<Basket> =
        restRepository.editPersonsQuantity(persons)

    suspend fun clearBasket(basketClearReq: BasketClearReq): Result<Basket> =
        restRepository.clearBasket(basketClearReq)

    suspend fun removeBasketItem(removeBasketItem: RemoveBasketItemReq): Result<Basket> =
        restRepository.removeBasketItem(removeBasketItem)

    fun getBasketPersonReq(basket: Basket, quantity: Int): BasketPersonsReq =
        restRepository.getBasketPersonReq(basket, quantity)

    fun getBasketClearReq(basket: Basket): BasketClearReq =
        restRepository.getBasketClearReq(basket)

    fun getRemoveBasketItemReq(product: BasketDish?, basket: Basket): RemoveBasketItemReq =
        restRepository.getRemoveBasketItemReq(product, basket)

    fun getBasketEditorReq(basket: Basket, addingProduct: BasketDish): BasketEditorReq =
        restRepository.getBasketEditorReq(basket, addingProduct)

    fun getUserPhone(): String? =
        prefsRepository.readUserPhone()
}