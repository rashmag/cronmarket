package ooo.cron.delivery.data

import com.google.gson.Gson
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketPersonsReq
import ooo.cron.delivery.data.network.models.RemoveBasketItemReq
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.utils.Error
import ooo.cron.delivery.utils.NoConnection
import ooo.cron.delivery.utils.Result
import ooo.cron.delivery.utils.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
        return try {
            if (response.isSuccessful && body != null)
                Success(body)
            else Error(Exception(), response.message())
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }


    suspend fun sendOrder(
        token: String,
        basketId: String,
        phone: String?,
        comment: String,
        deliveryCityId: String?,
        paymentMethod: Int
    ) =
        restService.sendOrder(
            token, OrderReq(
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
                null,
                paymentMethod
            )
        )

    suspend fun increaseProductInBasket(editor: BasketEditorReq): Result<Basket> {
        return try {
            val result = restService.increaseProductInBasket(editor)
            Success(result)
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }


    suspend fun decreaseProductInBasket(editor: BasketEditorReq): Result<Basket> {
        return try {
            val result = restService.decreaseProductInBasket(editor)
            Success(result)
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }

    suspend fun editPersonsQuantity(persons: BasketPersonsReq): Result<Basket> {
        return try {
            val result = restService.editPersonsCount(persons)
            Success(result)
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }

    suspend fun clearBasket(basketClearReq: BasketClearReq): Result<Basket> {
        return try {
            val result = restService.clearBasket(basketClearReq)
            Success(result)
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }

    suspend fun removeBasketItem(removeBasketItem: RemoveBasketItemReq): Result<Basket> {
        return try {
            val result = restService.removeProduct(removeBasketItem)
            Success(result)
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }

    fun getBasketPersonReq(basket:Basket, quantity: Int): BasketPersonsReq =
        BasketPersonsReq(
            basket.id,
            quantity
        )

    fun getBasketClearReq(basket: Basket): BasketClearReq =
        BasketClearReq(basket.id)

    fun getRemoveBasketItemReq(product: BasketDish?, basket: Basket): RemoveBasketItemReq =
        RemoveBasketItemReq(
            product?.id.orEmpty(),
            basket.id
        )

    fun getBasketEditorReq(basket: Basket, addingProduct: BasketDish): BasketEditorReq =
        BasketEditorReq(
            basket.id,
            basket.partnerId,
            basket.marketCategoryId,
            Gson().toJson(addingProduct)
        )
}