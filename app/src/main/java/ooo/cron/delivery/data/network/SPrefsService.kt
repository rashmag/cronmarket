package ooo.cron.delivery.data.network

import android.content.SharedPreferences
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.data.network.models.MarketCategory
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

    fun writeSelectedMarketCategory(category: MarketCategory) =
        sharedPreferences.edit()
            .putInt(MARKET_CATEGORY_ID, category.id)
            .putString(MARKET_CATEGORY_NAME, category.categoryName)
            .putString(MARKET_CATEGORY_IMAGE, category.categoryImgUri)
            .commit()

    fun readSelectedMarketCategory() =
        MarketCategory(
            sharedPreferences.getInt(MARKET_CATEGORY_ID, 1),
            sharedPreferences.getString(MARKET_CATEGORY_NAME, "")!!,
            sharedPreferences.getString(MARKET_CATEGORY_IMAGE, "")!!
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

    fun writeUserBasket(id: String) =
        sharedPreferences.edit()
            .putString(USER_BASKET, id)
            .commit()

    fun readUserBasket() =
        sharedPreferences.getString(USER_BASKET, null)

    fun removeBasketId() =
        sharedPreferences.edit().putString(USER_BASKET, EMPTY_UUID)
            .commit()

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
        const val EMPTY_UUID = "00000000-0000-0000-0000-000000000000"

        const val CITY_ID = "CITY_ID"
        const val CITY_NAME = "CITY_NAME"
        const val CITY_KLADR_ID = "CITY_KLADR_ID"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"

        const val USER_PHONE = "user_phone"
        const val USER_BASKET = "user_basket"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"

        const val MARKET_CATEGORY_ID = "market_category_id"
        const val MARKET_CATEGORY_NAME = "market_category_name"
        const val MARKET_CATEGORY_IMAGE = "market_category_image"
    }
}