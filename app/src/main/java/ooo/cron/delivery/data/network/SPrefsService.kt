package ooo.cron.delivery.data.network

import android.content.SharedPreferences
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.data.network.models.RefreshableToken
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 03.05.2021.
 */
class SPrefsService @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun writeChosenCity(city: City) =
        sharedPreferences.edit()
            .putString(CITY_ID, city.id)
            .putString(CITY_NAME, city.city)
            .putString(CITY_KLADR_ID, city.kladrId)
            .commit()

    fun readChosenCity() =
        City(
            sharedPreferences.getString(CITY_ID, "")!!,
            sharedPreferences.getString(CITY_NAME, "")!!,
            sharedPreferences.getString(CITY_KLADR_ID, "")!!
        )

    fun writeBuildingAddress(address: String) =
        sharedPreferences.edit()
            .putString(STREET_WITH_BUILDING, address)
            .commit()

    fun readBuildingAddress() =
        sharedPreferences.getString(STREET_WITH_BUILDING, "")

    fun writeUserPhone(phone: String) =
        sharedPreferences.edit()
            .putString(USER_PHONE, phone)
            .commit()

    fun readUserPhone() =
        sharedPreferences.getString(USER_PHONE, "")

    fun writeToken(token: RefreshableToken) =
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN, token.accessToken)
            .putString(REFRESH_TOKEN, token.refreshToken)
            .commit()

    fun readToken() =
        RefreshableToken(
            sharedPreferences.getString(ACCESS_TOKEN, "")!!,
            sharedPreferences.getString(REFRESH_TOKEN, "")!!
        )

    fun removeToken() =
        sharedPreferences.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .commit()

    companion object {
        const val CITY_ID = "CITY_ID"
        const val CITY_NAME = "CITY_NAME"
        const val CITY_KLADR_ID = "CITY_KLADR_ID"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"

        const val USER_PHONE = "user_phone"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }
}