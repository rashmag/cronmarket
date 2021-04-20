package ooo.cron.delivery.data.network.models

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */


data class City(val id: String, val city: String, val kladrId: String) {
    override fun toString() = city
}