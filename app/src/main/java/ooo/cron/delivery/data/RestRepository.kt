package ooo.cron.delivery.data

import com.google.gson.Gson
import okhttp3.ResponseBody
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketPersonsReq
import ooo.cron.delivery.data.network.models.RemoveBasketItemReq
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.utils.*
import retrofit2.HttpException
import retrofit2.Response
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
            OrderReq(
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
        return errorHandleResult(result)
    }


    suspend fun decreaseProductInBasket(editor: BasketEditorReq): Result<Basket> {
        val result = restService.decreaseProductInBasket(editor)
        return errorHandleResult(result)
    }

    suspend fun editPersonsQuantity(persons: BasketPersonsReq): Result<Basket> {
        val result = restService.editPersonsCount(persons)
        return errorHandleResult(result)
    }

    suspend fun clearBasket(basketClearReq: BasketClearReq): Result<Basket> {
        val result = restService.clearBasket(basketClearReq)
        return errorHandleResult(result)
    }

    suspend fun removeBasketItem(removeBasketItem: RemoveBasketItemReq): Result<Basket> {
        val result = restService.removeProduct(removeBasketItem)
        return errorHandleResult(result)
    }

/*    suspend fun likePartner(partnerId:String): Response<ResponseBody> {
        val response = restService.likePartner(partnerId)
        return restService.likePartner(partnerId)
    }

    suspend fun unlikePartner(partnerId: String): Response<ResponseBody> {
        val response = restService.unlikePartner(partnerId)
        return errorHandleResponse(response)
    }

    private fun errorHandleResponse(response: Response<ResponseBody>): Response<ResponseBody> {
            return try {
                if (response.isSuccessful)
                    response.body()
            } catch (e: Exception) {
                ErrorResponse(e,e.message)
            }

    }*/

    private fun errorHandleResult(result: Basket): Result<Basket> {
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