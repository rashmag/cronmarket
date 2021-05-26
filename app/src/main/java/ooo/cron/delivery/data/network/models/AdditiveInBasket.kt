package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Ramazan Gadzhikadiev on 26.05.2021.
 */
data class AdditiveInBasket(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Cost")
    val cost: Double
)