package ooo.cron.delivery.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ooo.cron.delivery.data.PrefsRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val repository: PrefsRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original : Request = chain.request()
        if (repository.isLogin() != null) {
            original = original.newBuilder()
                .addHeader(AUTHORIZATION_KEY, "Bearer ${repository.readToken()?.accessToken}")
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