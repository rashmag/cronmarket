package ooo.cron.delivery.data.network

import ooo.cron.delivery.data.network.models.SuggestAddress
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.data.network.models.MarketCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */

interface RestService {

    @GET("/api/v1/Address/delivery_cities")
    suspend fun getCities(): Response<List<City>>

    @GET("/api/v1/Address/suggest_address")
    suspend fun getSuggestAddress(
        @Query("KladrId") kladrId: String,
        @Query("Address") address: String
    ): Response<List<SuggestAddress>>

    @GET("/api/v1/Address/suggest_address_geolocate")
    suspend fun getSuggestAddress(
        @Query("Geolocate.Latitude") latitude: Double,
        @Query("Geolocate.Longitude") longitude: Double
    ): Response<List<SuggestAddress>>

    @GET("/api/v1/MainWindow/market_categories")
    suspend fun getMarketCategories(
        @Query("CityId") cityId: String
    ): Response<List<MarketCategory>>
}