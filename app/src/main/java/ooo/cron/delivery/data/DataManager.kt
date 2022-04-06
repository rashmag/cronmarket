package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.RestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ooo.cron.delivery.data.network.SPrefsService
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.*
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */

class DataManager @Inject constructor(
    private val restService: RestService,
    private val sPrefsService: SPrefsService
) {
    suspend fun getCities() =
        withContext(Dispatchers.IO) {
            restService.getCities()
        }

    suspend fun getSuggestAddress(
        kladrId: String,
        city: String
    ) =
        withContext(Dispatchers.IO) {
            restService.getSuggestAddress(kladrId, city)
        }

    suspend fun getSuggestAddress(
        latitude: Double,
        longitude: Double
    ) =
        withContext(Dispatchers.IO) {
            restService.getSuggestAddress(latitude, longitude)
        }

    suspend fun getMarketCategories(cityId: String) =
        withContext(Dispatchers.IO) {
            restService.getMarketCategories(cityId)
        }

    suspend fun getTagsResponse(
        cityId: String,
        marketCategoryId: Int
    ) =
        withContext(Dispatchers.IO) {
            restService.getTags(cityId, marketCategoryId)
        }

    suspend fun getAllPartners(
        cityId: String,
        marketCategoryId: Int,
        offset: Int
    ) =
        withContext(Dispatchers.IO) {
            restService.getPartners(cityId, marketCategoryId, offset)
        }

    suspend fun getPartnersByTag(
        cityId: String,
        marketCategoryId: Int,
        tagId: String,
        offset: Int
    ) =
        withContext(Dispatchers.IO) {
            restService.getPartnersByTag(cityId, marketCategoryId, tagId, offset)
        }

    suspend fun getUser(token: String) =
        withContext(Dispatchers.IO) {
            restService.getUser(token)
        }

    suspend fun refreshToken(token: RefreshableToken) =
        withContext(Dispatchers.IO) {
            restService.refreshToken(token)
        }

    suspend fun getPartnersInfo(partnerId: String) = withContext(Dispatchers.IO) {
        restService.getPartnersInfo(partnerId)
    }

    suspend fun getPartnerCategory(partnerId: String) = withContext(Dispatchers.IO) {
        restService.getPartnersCategory(partnerId)
    }

    suspend fun getPartnerProducts(partnerId: String) = withContext(Dispatchers.IO) {
        restService.getPartnerProducts(partnerId)
    }

    suspend fun sendOrder(token: String, orderReq: OrderReq) = withContext(Dispatchers.IO) {
        restService.sendOrder(token, orderReq)
    }

    fun sentCode(sentCodeReq: SentCodeReq): Call<Void> {
        return restService.sentCode(sentCodeReq)
    }

    fun sentConfirmCode(confirmCodeReq: ConfirmCodeReq): Call<RefreshableToken> {
        return restService.sentConfirmCode(confirmCodeReq)
    }

    fun setUserName(token: String, userName: SetUserNameReq): Call<Void> {
        return restService.setUserName(token, userName)
    }

    suspend fun logOut(refreshToken: LogOutReq): Response<ResponseBody> =
        restService.logOut(refreshToken)

    suspend fun getSpecialOffers(cityId: String, marketCategoryId: Int) =
        restService.getSpecialOffers(cityId, marketCategoryId).promotions

    suspend fun getBasket(basketId: String): Response<Basket> =
        restService.getBasket(basketId)

    suspend fun increaseProductInBasket(token: String, editor: BasketEditorReq) =
        withContext(Dispatchers.IO) {
            restService.increaseProductInBasket(token, editor)
        }

    suspend fun increaseProductInBasket(editor: BasketEditorReq) =
        withContext(Dispatchers.IO) {
            restService.increaseProductInBasket(editor)
        }

    suspend fun decreaseProductInBasket(editor: BasketEditorReq) =
        withContext(Dispatchers.IO) {
            restService.decreaseProductInBasket(editor)
        }

    suspend fun editPersonsQuantity(persons: BasketPersonsReq) =
        withContext(Dispatchers.IO) {
            restService.editPersonsCount(persons)
        }

    suspend fun removeBasketItem(requestBody: RemoveBasketItemReq) =
        withContext(Dispatchers.IO) {
            restService.removeProduct(requestBody)
        }

    suspend fun clearBasket(basketClearReq: BasketClearReq) =
        withContext(Dispatchers.IO) {
            restService.clearBasket(basketClearReq)
        }

    suspend fun getStableVersion() =
        withContext(Dispatchers.IO) {
            restService.getStableVersion()
        }

    suspend fun writeChosenCity(city: City) =
        withContext(Dispatchers.IO) {
            sPrefsService.writeChosenCity(city)
        }

    suspend fun readChosenCity() =
        withContext(Dispatchers.IO) {
            sPrefsService.readChosenCity()
        }

    suspend fun writeCurrentCityId(cityId: String) =
        withContext(Dispatchers.IO){
            sPrefsService.writeCurrentCityId(cityId)
        }

    suspend fun readCurrentCityId() =
        withContext(Dispatchers.IO){
            sPrefsService.readCurrentCityId()
        }

    fun writeCurrentCityPosition(position: Int){
        sPrefsService.writeCurrentCityPosition(position)
    }

    fun readCurrentCityPosition() =
        sPrefsService.readCurrentCityPosition()

    fun readChosenCityId() =
        sPrefsService.readChosenCity()?.id

    fun writeSelectedMarketCategory(category: MarketCategory) =
        sPrefsService.writeSelectedMarketCategory(category)

    fun readSelectedMarketCategory() =
        sPrefsService.readSelectedMarketCategory()

    suspend fun writeBuildingAddress(address: String) =
        withContext(Dispatchers.IO) {
            sPrefsService.writeBuildingAddress(address)
        }

    suspend fun readBuildingAddress() =
        withContext(Dispatchers.IO) {
            sPrefsService.readBuildingAddress()
        }

    fun readAddress() =
        sPrefsService.readBuildingAddress()

    fun readUserPhone() =
        sPrefsService.readUserPhone()

    fun writeUserPhone(phone: String) {
        sPrefsService.writeUserPhone(phone)
    }

    fun readBasket() =
        sPrefsService.readBasket()

    fun writeBasket(basket: Basket) {
        sPrefsService.writeBasket(basket)
    }

    fun readUserBasketId() =
        sPrefsService.readUserBasketId() ?: EMPTY_UUID

    fun writeUserBasketId(id: String) =
        sPrefsService.writeUserBasketId(id)

    fun removeUserBasketId() {
        sPrefsService.removeUserBasketId()
    }

    fun writeToken(token: RefreshableToken) {
        sPrefsService.writeToken(token)
    }

    fun readToken() =
        sPrefsService.readToken()

    fun removeToken() =
        sPrefsService.removeToken()

    fun writePartnerId(id: String){
        sPrefsService.writePartnerId(id)
    }

    fun readPartnerId() = sPrefsService.readPartnerId()

    fun writePartnerOpenHours(openHours: Int){
        sPrefsService.writePartnerOpenHours(openHours)
    }

    fun readPartnerOpenHours() =
        sPrefsService.readPartnerOpenHours()

    fun writePartnerCloseTime(closeHours: Int){
        sPrefsService.writePartnerCloseHours(closeHours)
    }

    fun readPartnerCloseHours() =
        sPrefsService.readPartnerCloseHours()

    companion object {
        const val EMPTY_UUID = SPrefsService.EMPTY_UUID
    }
}