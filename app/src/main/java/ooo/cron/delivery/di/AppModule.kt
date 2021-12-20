package ooo.cron.delivery.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ooo.cron.delivery.BuildConfig.BASE_URL
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.OrderPrefsRepository
import ooo.cron.delivery.data.OrderRestRepository
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.SPrefsService
import ooo.cron.delivery.data.network.errors.ApiErrorsUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRestService(retrofit: Retrofit): RestService =
        retrofit.create(RestService::class.java)

    @Provides
    @Singleton
    fun providePreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(ooo.cron.delivery.BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSPrefsService(sharedPreferences: SharedPreferences) =
        SPrefsService(sharedPreferences)

    @Provides
    @Singleton
    fun provideOrderPrefsRepository(sharedPreferences: SharedPreferences) =
        OrderPrefsRepository(sharedPreferences)

    @Provides
    @Singleton
    fun provideDataManager(restService: RestService, sPrefsService: SPrefsService) =
        DataManager(restService, sPrefsService)

    @Provides
    @Singleton
    fun provideOrderRestRepository(restService: RestService) =
        OrderRestRepository(restService)

    @Provides
    @Singleton
    fun provideApiErrorUtils(): ApiErrorsUtils {
        return ApiErrorsUtils()
    }
}