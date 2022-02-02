package ooo.cron.delivery.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ooo.cron.delivery.data.network.SPrefsService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.data.network.models.RefreshableToken
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 17.12.2021
 * */

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

    fun readToken(): RefreshableToken? =
        Gson().fromJson(sharedPreferences.getString(TOKEN, ""), RefreshableToken::class.java)

    fun readDeliveryCity() =
        Gson().fromJson(sharedPreferences.getString(CITY, ""), City::class.java)

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

        const val TOKEN = "token"

        const val BASKET = "basket"

        const val USER_BASKET_ID = "user_basket"

        const val CITY_ID = "CITY_ID"

        const val CITY = "city"
    }
}