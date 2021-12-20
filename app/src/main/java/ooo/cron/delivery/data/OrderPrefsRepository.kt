package ooo.cron.delivery.data

import android.content.SharedPreferences
import ooo.cron.delivery.data.network.models.RefreshableToken
import javax.inject.Inject

class OrderPrefsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun readUserBasketId() =
        sharedPreferences.getString(USER_BASKET, "") ?: EMPTY_UUID

    fun readUserPhone() =
        sharedPreferences.getString(USER_PHONE, "")

    fun readBuildingAddress() =
        sharedPreferences.getString(STREET_WITH_BUILDING, "")

    fun readToken() =
        RefreshableToken(
            sharedPreferences.getString(ACCESS_TOKEN, "")!!,
            sharedPreferences.getString(REFRESH_TOKEN, "")!!
        )

    companion object {
        const val EMPTY_UUID = "00000000-0000-0000-0000-000000000000"

        const val USER_BASKET = "user_basket"

        const val USER_PHONE = "user_phone"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }
}