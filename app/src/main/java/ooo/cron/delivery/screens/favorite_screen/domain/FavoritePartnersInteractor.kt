package ooo.cron.delivery.screens.favorite_screen.domain

import ooo.cron.delivery.data.network.models.FavoritePartners
import ooo.cron.delivery.screens.favorite_screen.data.repo.FavoritePartnersRepository
import retrofit2.Response
import ooo.cron.delivery.utils.Result
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 02.04.2021
 * */

class FavoritePartnersInteractor @Inject constructor(
    private val favoritePartnersRepository: FavoritePartnersRepository
) {
    suspend fun getFavoritePartners( cityId: String) : Result<FavoritePartners> {
        return favoritePartnersRepository.getFavoritePartners(cityId)
    }
}