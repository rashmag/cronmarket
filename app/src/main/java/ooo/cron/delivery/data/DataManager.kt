package ooo.cron.delivery.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ooo.cron.delivery.data.network.RestService
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */

class DataManager @Inject constructor(private val restService: RestService) {
    suspend fun getCities() = withContext(Dispatchers.IO) {
        restService.getCities()
    }

    suspend fun getSuggestAddress(kladrId: String, city: String) = withContext(Dispatchers.IO) {
        restService.getSuggestAddress(kladrId, city)
    }

    suspend fun getSuggestAddress(latitude: Double, longitude: Double) = withContext(Dispatchers.IO) {
        restService.getSuggestAddress(latitude, longitude)
    }
}