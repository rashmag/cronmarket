package ooo.cron.delivery.data.network.models

/**
 * Created by Ramazan Gadzhikadiev on 20.05.2021.
 */

data class Basket(
    val id: String,
    val marketCategoryId: Int,
    val partnerId: String,
    val amount: Int,
    val cutleryCount: Int,
    val content: String
)