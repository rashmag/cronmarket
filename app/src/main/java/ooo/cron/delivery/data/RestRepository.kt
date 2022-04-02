package ooo.cron.delivery.data

import com.google.gson.Gson
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.utils.Error
import ooo.cron.delivery.utils.NoConnection
import ooo.cron.delivery.utils.Result
import ooo.cron.delivery.utils.Success
import retrofit2.HttpException
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
        } catch (e: HttpException) {
            Error(e, e.message)
        }
    }

    suspend fun sendOrder(
        token: String,
        basketId: String,
        phone: String?,
        comment: String,
        deliveryCityId: String?,
        address: String?,
        entrance: String?,
        floor: String?,
        flat: String?,
        deliveryAtTime: String?,
        saveAddress: Boolean?,
        discount: Int?,
        paymentMethod: Int

    ) {
        restService.sendOrder(
            token, OrderReq(
                basketId,
                phone,
                comment,
                deliveryCityId,
                address,
                entrance,
                floor,
                flat,
                deliveryAtTime,
                saveAddress,
                discount,
                paymentMethod
            )
        )
    }

    suspend fun increaseProductInBasket(editor: BasketEditorReq): Result<Basket> {
        val result = restService.increaseProductInBasket(editor)
        return errorHandle(result)
    }


    suspend fun decreaseProductInBasket(editor: BasketEditorReq): Result<Basket> {
        val result = restService.decreaseProductInBasket(editor)
        return errorHandle(result)
    }

    suspend fun editPersonsQuantity(persons: BasketPersonsReq): Result<Basket> {
        val result = restService.editPersonsCount(persons)
        return errorHandle(result)
    }

    suspend fun clearBasket(basketClearReq: BasketClearReq): Result<Basket> {
        val result = restService.clearBasket(basketClearReq)
        return errorHandle(result)
    }

    suspend fun removeBasketItem(removeBasketItem: RemoveBasketItemReq): Result<Basket> {
        val result = restService.removeProduct(removeBasketItem)
        return errorHandle(result)
    }

    private fun errorHandle(result: Basket): Result<Basket> {
        return try {
            Success(result)
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: Exception) {
            Error(e, e.message)
        }
    }

    fun getBasketPersonReq(basket:Basket, quantity: Int): BasketPersonsReq {
        return BasketPersonsReq(
            basket.id,
            quantity
        )
    }

    fun getBasketClearReq(basket: Basket): BasketClearReq {
        return BasketClearReq(basket.id)
    }

    fun getRemoveBasketItemReq(product: BasketDish?, basket: Basket): RemoveBasketItemReq {
        return RemoveBasketItemReq(
            product?.id.orEmpty(),
            basket.id
        )
    }

    fun getBasketEditorReq(basket: Basket, addingProduct: BasketDish): BasketEditorReq {
        return BasketEditorReq(
            basket.id,
            basket.partnerId,
            basket.marketCategoryId,
            Gson().toJson(addingProduct)
        )
    }
}