package ooo.cron.delivery.data.network.request

/**
 * Created by Ramazan Gadzhikadiev on 29.05.2021.
 */

data class BasketEditorReq(
    val basketId: String,
    val partnerId: String,
    val marketCategoryId: Int,
    val product: String
)