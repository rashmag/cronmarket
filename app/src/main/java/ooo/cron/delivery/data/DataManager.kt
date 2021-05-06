package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.request.ConfirmCodeReq
import ooo.cron.delivery.data.network.request.SentCodeReq
import ooo.cron.delivery.data.network.request.SetUserNameReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ooo.cron.delivery.data.network.SPrefsService
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.data.network.models.RefreshableToken
import ooo.cron.delivery.data.network.request.LogOutReq
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

    suspend fun writeChosenCity(city: City) =
        withContext(Dispatchers.IO) {
            sPrefsService.writeChosenCity(city)
        }

    suspend fun readChosenCity() =
        withContext(Dispatchers.IO) {
            sPrefsService.readChosenCity()
        }

    suspend fun writeBuildingAddress(address: String) =
        withContext(Dispatchers.IO) {
            sPrefsService.writeBuildingAddress(address)
        }

    suspend fun readBuildingAddress() =
        withContext(Dispatchers.IO) {
            sPrefsService.readBuildingAddress()
        }

    fun readUserPhone()=
        sPrefsService.readUserPhone()

    fun writeUserPhone(phone: String) {
        sPrefsService.writeUserPhone(phone)
    }

    fun writeToken(token: RefreshableToken) {
        sPrefsService.writeToken(token)
    }

    fun readToken() =
        sPrefsService.readToken()

    fun removeToken() =
        sPrefsService.removeToken()
}