package ooo.cron.delivery.screens.favorite_screen.data.repo

import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.FavoritePartners
import ooo.cron.delivery.utils.Error
import ooo.cron.delivery.utils.NoConnection
import ooo.cron.delivery.utils.Result
import ooo.cron.delivery.utils.Success
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 02.04.2022
 * */

class FavoritePartnersRepository @Inject constructor(
    private val api: RestService
) {
    suspend fun getFavoritePartners( cityId: String) : Result<FavoritePartners> {
        val response = api.getFavoritePartners(cityId)
        val body = response.body()
        return try {
            if (response.isSuccessful && body != null)
                Success(body)
            else Error(Exception(), response.message())
        } catch (e: UnknownHostException) {
            NoConnection
        } catch (e: SocketTimeoutException) {
            NoConnection
        } catch (e: HttpException) {
            Error(e, e.message)
        }
    }
}