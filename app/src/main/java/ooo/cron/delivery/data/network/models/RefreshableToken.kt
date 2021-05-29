package ooo.cron.delivery.data.network.models

/**
 * Created by Ramazan Gadzhikadiev on 03.05.2021.
 */

data class RefreshableToken(
    var accessToken: String,
    var refreshToken: String
)