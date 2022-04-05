package ooo.cron.delivery.data.network

import ooo.cron.delivery.data.PrefsRepository
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 03.04.2022
 * */

class AuthInteractor @Inject constructor(
    private val repository: PrefsRepository
) {
    fun getToken(): String? {
        return repository.readToken()?.accessToken
    }

    fun isLogged(): Boolean {
        return repository.readToken()?.accessToken?.isNotEmpty() ?: false
    }
}