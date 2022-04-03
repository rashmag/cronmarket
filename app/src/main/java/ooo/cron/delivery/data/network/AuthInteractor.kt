package ooo.cron.delivery.data.network

import ooo.cron.delivery.data.PrefsRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val repository: PrefsRepository
) {
    fun getToken(): String? {
        return repository.readToken()?.accessToken
    }

    fun isLogin(): Boolean {
        return repository.isLogin()
    }
}