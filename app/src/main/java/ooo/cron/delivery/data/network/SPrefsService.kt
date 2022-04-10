package ooo.cron.delivery.data.network

import android.content.SharedPreferences
import com.google.gson.Gson
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
    fun writeChosenCity(city: City) {
        sharedPreferences.edit()
            .putString(CITY, Gson().toJson(city).toString())
            .apply()
    }

    fun readChosenCity(): City? {
        val city = sharedPreferences.getString(CITY, "")
        if (city.isNullOrEmpty().not()) {
            return Gson().fromJson(city, City::class.java)
        }
        val cityIdPrevious = sharedPreferences.getString(CITY_ID, "")
        if (cityIdPrevious != null && cityIdPrevious.isEmpty()) {
            return null
        }
        val cityNamePrevious = sharedPreferences.getString(CITY_NAME, "")
        if (cityNamePrevious != null && cityNamePrevious.isEmpty()) {
            return null
        }
        val cityKladrIdPrevious = sharedPreferences.getString(CITY_KLADR_ID, "")
        if (cityKladrIdPrevious != null && cityKladrIdPrevious.isEmpty()) {
            return null
        }
        val cityPrevious = City(
            cityIdPrevious.toString(),
            cityNamePrevious.toString(),
            cityKladrIdPrevious.toString()
        )
        writeChosenCity(cityPrevious)
        return cityPrevious
    }


    fun writeCurrentCityId(cityId: String) =
        sharedPreferences.edit()
            .putString(CURRENT_CITY_ID, cityId)
            .commit()

    fun readCurrentCityId() =
        sharedPreferences.getString(CURRENT_CITY_ID, "")

    fun writeCurrentCityPosition(position: Int) =
        sharedPreferences.edit()
            .putInt(CURRENT_CITY_POSITION, position)
            .commit()

    fun readCurrentCityPosition() =
        sharedPreferences.getInt(CURRENT_CITY_POSITION, -1)

    fun writeSelectedMarketCategory(category: MarketCategory) =
        sharedPreferences.edit()
            .putString(MARKET_CATEGORY, Gson().toJson(category).toString())
            .apply()

    fun readSelectedMarketCategory(): MarketCategory? {
        val marketCategory = sharedPreferences.getString(MARKET_CATEGORY, "")
        if (marketCategory.isNullOrEmpty().not()) {
            return Gson().fromJson(
                marketCategory,
                MarketCategory::class.java)
        }
        val marketCategoryIdPrevious = sharedPreferences.getInt(MARKET_CATEGORY_ID, -1)
        if (marketCategoryIdPrevious == -1) {
            return null
        }
        val marketCategoryNamePrevious = sharedPreferences.getString(MARKET_CATEGORY_NAME, "")
        if (marketCategoryNamePrevious != null && marketCategoryNamePrevious.isEmpty()) {
            return null
        }
        val marketCategoryImagePrevious = sharedPreferences.getString(MARKET_CATEGORY_IMAGE, "")
        if (marketCategoryImagePrevious != null && marketCategoryImagePrevious.isEmpty()) {
            return null
        }
        val marketCategoryPrevious =
            MarketCategory(
                marketCategoryIdPrevious,
                marketCategoryNamePrevious.toString(),
                marketCategoryImagePrevious.toString()
            )
        writeSelectedMarketCategory(marketCategoryPrevious)
        return marketCategoryPrevious
    }

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

    fun writeFirstOpenApp(){
        sharedPreferences.edit()
            .putBoolean(FIRST_OPEN_APP,false)
            .apply()
    }

    fun readFirstOpenApp() = sharedPreferences
        .getBoolean(FIRST_OPEN_APP,true)

    fun writeBasket(basket: Basket) {
        sharedPreferences.edit()
            .putString(BASKET, Gson().toJson(basket).toString())
            .apply()
    }

    fun readBasket() =
        Gson().fromJson(sharedPreferences.getString(BASKET, ""), Basket::class.java)

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
            .putString(TOKEN, Gson().toJson(token).toString())
            .apply()

    fun readToken(): RefreshableToken? {
        val token = sharedPreferences.getString(TOKEN, "")
        if (token.isNullOrEmpty().not()) {
            return Gson().fromJson(token, RefreshableToken::class.java)
        }
        val tokenPreviousRefresh = sharedPreferences.getString(REFRESH_TOKEN, "")
        if (tokenPreviousRefresh != null && tokenPreviousRefresh.isEmpty()) {
            return null
        }
        val tokenPreviousAccess = sharedPreferences.getString(ACCESS_TOKEN, "")
        if (tokenPreviousAccess != null && tokenPreviousAccess.isEmpty()) {
            return null
        }
        val tokenPrevious = RefreshableToken(
            tokenPreviousAccess.toString(),
            tokenPreviousRefresh.toString()
        )
        writeToken(tokenPrevious)
        return tokenPrevious
    }

    fun removeToken() =
        sharedPreferences.edit()
            .remove(TOKEN)
            .apply()

    fun writePartnerId(id: String){
        sharedPreferences.edit()
            .putString(PARTNER_ID, id)
            .commit()
    }

    fun writePartnerOpenHours(openHours: Int){
        sharedPreferences.edit()
            .putInt(PARTNER_OPEN_HOURS, openHours)
            .commit()
    }

    fun readPartnerOpenHours() =
        sharedPreferences.getInt(PARTNER_OPEN_HOURS, 0)

    fun writePartnerCloseHours(closeHours: Int){
        sharedPreferences.edit()
            .putInt(PARTNER_CLOSE_HOURS, closeHours)
            .commit()
    }

    fun readPartnerCloseHours() =
        sharedPreferences.getInt(PARTNER_CLOSE_HOURS, 0)

    fun readPartnerId() = sharedPreferences.getString(PARTNER_ID, "")

    companion object {
        const val EMPTY_UUID = "00000000-0000-0000-0000-000000000000"

        const val CITY_ID = "CITY_ID"
        const val CITY_NAME = "CITY_NAME"
        const val CITY_KLADR_ID = "CITY_KLADR_ID"

        const val CITY = "city"

        const val CURRENT_CITY_ID = "CURRENT_CITY_ID"

        const val CURRENT_CITY_POSITION = "CURRENT_CITY_POSITION"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"

        const val USER_PHONE = "user_phone"

        const val BASKET = "basket"

        const val USER_BASKET = "user_basket"

        const val FIRST_OPEN_APP = "first_open_app"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"

        const val TOKEN = "token"

        const val MARKET_CATEGORY_ID = "market_category_id"
        const val MARKET_CATEGORY_NAME = "market_category_name"
        const val MARKET_CATEGORY_IMAGE = "market_category_image"

        const val MARKET_CATEGORY = "market_category"

        const val PARTNER_ID = "PARTNER_ID"

        const val PARTNER_OPEN_HOURS = "PARTNER_OPEN_HOURS"
        const val PARTNER_CLOSE_HOURS = "PARTNER_CLOSE_HOURS"
    }
}