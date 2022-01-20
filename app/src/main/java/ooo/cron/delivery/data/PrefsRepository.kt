package ooo.cron.delivery.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ooo.cron.delivery.data.network.SPrefsService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.RefreshableToken
import javax.inject.Inject

class PrefsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun readBasket() =
        Gson().fromJson(sharedPreferences.getString(BASKET, ""), Basket::class.java)

    fun readUserPhone() =
        sharedPreferences.getString(USER_PHONE, "")

    fun readBuildingAddress() =
        sharedPreferences.getString(STREET_WITH_BUILDING, "")

    fun readToken() =
        RefreshableToken(
            sharedPreferences.getString(ACCESS_TOKEN, "")!!,
            sharedPreferences.getString(REFRESH_TOKEN, "")!!
        )

    fun readDeliveryCityId() =
        sharedPreferences.getString(CITY_ID, "")

    fun writeBasket(basket:Basket) {
        sharedPreferences.edit()
            .putString(BASKET, Gson().toJson(basket).toString())
            .apply()
    }

    companion object {
        const val USER_PHONE = "user_phone"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"

        const val BASKET = "basket"

        const val CITY_ID = "CITY_ID"
    }
}