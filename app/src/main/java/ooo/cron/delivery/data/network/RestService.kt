package ooo.cron.delivery.data.network

import ooo.cron.delivery.data.network.models.*
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

    @GET("/api/v1/MainWindow/tags")
    suspend fun getTags(
        @Query("CityId") cityId: String,
        @Query("MarketCategoryId") marketCategoryId: Int
    ): Response<TagsResult>

    @GET("/api/v1/MainWindow/partners")
    suspend fun getPartners(
        @Query("CityId") cityId: String,
        @Query("MarketCategoryId") marketCategoryId: Int,
        @Query("Offset") offset: Int,
        @Query("Limit") limit: Int = PARTNERS_PAGINATION_LIMIT
    ): Response<PartnerResult>

    @GET("/api/v1/MainWindow/partners_by_tag")
    suspend fun getPartnersByTag(
        @Query("CityId") cityId: String,
        @Query("MarketCategoryId") marketCategoryId: Int,
        @Query("TagId") tagId: String,
        @Query("Offset") offset: Int,
        @Query("Limit") limit: Int = PARTNERS_PAGINATION_LIMIT
    ): Response<PartnerResult>

    companion object {
        const val PARTNERS_PAGINATION_LIMIT = 15
    }
}