package ooo.cron.delivery.data.network.models

/**
 * Created by Ramazan Gadzhikadiev on 03.05.2021.
 */
data class User(
    val id: String,
    val name: String,
    val phone: String,
    val lastMarketCategoryId: Int,
    val lastDeliveryCityId: String,
)