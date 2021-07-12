package ooo.cron.delivery.data.network.models

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
data class Tag(val id: String, val name: String) {
    override fun toString() =
        name
}