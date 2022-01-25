package ooo.cron.delivery.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.RefreshableToken
import javax.inject.Inject

class PrefsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun readBasket(): Basket? {
        val basketPrefs = sharedPreferences.getString(BASKET, null)
        return if (basketPrefs != null)
            Gson().fromJson(basketPrefs, Basket::class.java)
        else null
    }

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

    fun writeBasket(basket: Basket) {
        sharedPreferences.edit()
            .putString(BASKET, Gson().toJson(basket).toString())
            .apply()
    }

    fun readBasketId() =
        sharedPreferences.getString(USER_BASKET_ID, null)

    companion object {
        const val USER_PHONE = "user_phone"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"

        const val BASKET = "basket"

        const val USER_BASKET_ID = "user_basket"

        const val CITY_ID = "CITY_ID"
    }
}