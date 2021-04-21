package ooo.cron.delivery.data

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.City
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */

class DataManager @Inject constructor(
    private val restService: RestService,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getCities() = withContext(Dispatchers.IO) {
        restService.getCities()
    }

    suspend fun getSuggestAddress(kladrId: String, city: String) = withContext(Dispatchers.IO) {
        restService.getSuggestAddress(kladrId, city)
    }

    suspend fun getSuggestAddress(latitude: Double, longitude: Double) =
        withContext(Dispatchers.IO) {
            restService.getSuggestAddress(latitude, longitude)
        }

    suspend fun getMarketCategories(cityId: String) = withContext(Dispatchers.IO) {
        restService.getMarketCategories(cityId)
    }

    suspend fun writeChosenCity(city: City) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(CITY_ID, city.id)
            .putString(CITY_NAME, city.city)
            .putString(CITY_KLADR_ID, city.kladrId)
            .commit()
    }

    suspend fun readChosenCity() = withContext(Dispatchers.IO) {
        City(
            sharedPreferences.getString(CITY_ID, "")!!,
            sharedPreferences.getString(CITY_NAME, "")!!,
            sharedPreferences.getString(CITY_KLADR_ID, "")!!
        )
    }

    suspend fun writeBuildingAddress(address: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(STREET_WITH_BUILDING, address)
            .commit()
    }

    suspend fun readBuildingAddress() = withContext(Dispatchers.IO) {
         sharedPreferences.getString(STREET_WITH_BUILDING,"")
    }

    companion object {
        const val CITY_ID = "CITY_ID"
        const val CITY_NAME = "CITY_NAME"
        const val CITY_KLADR_ID = "CITY_KLADR_ID"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"
    }
}