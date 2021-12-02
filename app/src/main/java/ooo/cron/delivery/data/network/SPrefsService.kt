package ooo.cron.delivery.data.network

import android.content.SharedPreferences
import ooo.cron.delivery.data.network.models.Basket
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

    fun writeBasket(basket: Basket) {
        sharedPreferences.edit()
            .putString(BASKET_ID, basket.id)
            .putInt(BASKET_MARKET_CATEGORY_ID, basket.marketCategoryId)
            .putString(BASKET_PARTNER_ID, basket.partnerId)
            .putFloat(BASKET_AMOUNT, basket.amount.toFloat())
            .putFloat(BASKET_DELIVERY_COST, basket.deliveryCost.toFloat())
            .putInt(BASKET_CUTLERY_COUNT, basket.cutleryCount)
            .putString(BASKET_CONTENT, basket.content)
            .commit()

    }

    fun readBasket() =
        Basket(
            sharedPreferences.getString(BASKET_ID, "").toString(),
            sharedPreferences.getInt(BASKET_MARKET_CATEGORY_ID, 1),
            sharedPreferences.getString(BASKET_PARTNER_ID, "").toString(),
            sharedPreferences.getFloat(BASKET_AMOUNT, 1F).toDouble(),
            sharedPreferences.getFloat(BASKET_DELIVERY_COST, 1F).toDouble(),
            sharedPreferences.getInt(BASKET_CUTLERY_COUNT, 1),
            sharedPreferences.getString(BASKET_CONTENT, "").toString()
        )


    fun writeUserBasketId(id: String) =
        sharedPreferences.edit()
            .putString(USER_BASKET, id)
            .commit()

    fun readUserBasketId() =
        sharedPreferences.getString(USER_BASKET, null)

    fun removeUserBasketId() =
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

        const val BASKET_ID = "basket_id"
        const val BASKET_MARKET_CATEGORY_ID = "basket_marketCategoryId"
        const val BASKET_PARTNER_ID = "basket_partner_id"
        const val BASKET_AMOUNT = "basket_amount"
        const val BASKET_DELIVERY_COST = "basket_delivery_cost"
        const val BASKET_CUTLERY_COUNT = "basket_cutlery_count"
        const val BASKET_CONTENT = "basket_content"

        const val USER_BASKET = "user_basket"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"

        const val MARKET_CATEGORY_ID = "market_category_id"
        const val MARKET_CATEGORY_NAME = "market_category_name"
        const val MARKET_CATEGORY_IMAGE = "market_category_image"
    }
}