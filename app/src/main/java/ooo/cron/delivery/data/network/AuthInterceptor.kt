package ooo.cron.delivery.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 03.04.2022
 * */

class AuthInterceptor @Inject constructor(
    private val interactor: AuthInteractor
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original : Request = chain.request()
        if (interactor.isLogged()) {
            original = original.newBuilder()
                .addHeader(AUTHORIZATION_KEY, "Bearer ${interactor.getToken()}")
                .build()
        }
        val originalHttpUrl: HttpUrl = original.url
        val url: HttpUrl = originalHttpUrl.newBuilder()
            .build()
        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)
        val request: Request = requestBuilder
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val AUTHORIZATION_KEY = "Authorization"
    }
}