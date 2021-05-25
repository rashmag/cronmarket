package ooo.cron.delivery.data.network.models

/**
 * Created by Ramazan Gadzhikadiev on 15.04.2021.
 */
data class SuggestAddress(
    val fullAddress: String,
    val city: String,
    val cityWithType: String = "",
    val streetWithType: String = "",
    val house: String =""
) {
    override fun toString() = "$streetWithType $house"
}