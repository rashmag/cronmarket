package ooo.cron.delivery.data.network

import okhttp3.ResponseBody
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */

interface RestService {

    @GET("/api/v1/User")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>

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

    @POST("/api/v1/Account/send_code")
    fun sentCode(@Body sentCodeReq: SentCodeReq): Call<Void>

    @POST("/api/v1/Account/confirm_code")
    fun sentConfirmCode(@Body sentConformCodeReq: ConfirmCodeReq): Call<RefreshableToken>

    @POST("/api/v1/User/name")
    fun setUserName(@Header("Authorization") token: String, @Body name: SetUserNameReq): Call<Void>

    @GET("/api/v1/PartnerCard/partnerInfo")
    suspend fun getPartnersInfo(
        @Query("PartnerId") partnerId: String
    ): Response<PartnersInfoRes>

    @GET("/api/v1/PartnerCard/partner_products_categories")
    suspend fun getPartnersCategory(@Query("PartnerId") PartnerId: String):
            Response<PartnerCategoryRes>

    @GET("/api/v1/PartnerCard/partner_products")
    suspend fun getPartnerProducts(@Query("PartnerId") partnerId: String):
            Response<List<PartnerProductsRes>>

    @POST("/api/v1/Account/refresh_token")
    suspend fun refreshToken(
        @Body token: RefreshableToken
    ): Response<RefreshableToken>

    @POST("/api/v1/Account/logout")
    suspend fun logOut(
        @Body refreshToken: LogOutReq
    ): Response<ResponseBody>


    @POST("/api/v1/Order")
    suspend fun sendOrder(
        @Header("Authorization") token: String,
        @Body orderReq: OrderReq
    ): Response<ResponseBody>

    @GET("/api/v1/Basket")
    suspend fun getBasket(
        @Query("BasketId") basketId: String
    ): Response<Basket>

    @GET("/api/v1/Basket")
    suspend fun getSimpleBasket(
        @Query("BasketId") basketId: String
    ): Basket

    @POST("/api/v1/Basket/inc_product")
    suspend fun increaseProductInBasket(
        @Body editor: BasketEditorReq
    ): Basket

    @POST("/api/v1/Basket/inc_product")
    suspend fun increaseProductInBasket(
        @Header("Authorization") token: String,
        @Body editor: BasketEditorReq
    ): Basket

    @POST("/api/v1/Basket/dec_product")
    suspend fun decreaseProductInBasket(
        @Body editor: BasketEditorReq
    ): Basket

    @POST("/api/v1/Basket/cutlery")
    suspend fun editPersonsCount(
        @Body persons: BasketPersonsReq
    ): Basket

    @POST("/api/v1/Basket/remove_product")
    suspend fun removeProduct(
        @Body requestBody: RemoveBasketItemReq
    ): Basket

    @POST("/api/v1/Basket/clear")
    suspend fun clearBasket(
        @Body basketClearReq: BasketClearReq
    ): Basket

    @GET("/api/v1/SystemInfo/version")
    suspend fun getStableVersion(): String

    @GET("/api/v1/MainWindow/promotions")
    suspend fun getSpecialOffers(@Query("CityId") cityId: String)
            : PromotionsResponse

    companion object {
        const val PARTNERS_PAGINATION_LIMIT = 15
    }
}