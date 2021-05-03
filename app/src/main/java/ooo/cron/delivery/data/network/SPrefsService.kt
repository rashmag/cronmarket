package ooo.cron.delivery.data.network

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ooo.cron.delivery.data.network.models.City
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

    companion object {
        const val CITY_ID = "CITY_ID"
        const val CITY_NAME = "CITY_NAME"
        const val CITY_KLADR_ID = "CITY_KLADR_ID"

        const val STREET_WITH_BUILDING = "STREET_WITH_BUILDING"
    }
}