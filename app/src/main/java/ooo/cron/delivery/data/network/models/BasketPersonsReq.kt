package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Ramazan Gadzhikadiev on 03.06.2021.
 */
data class BasketPersonsReq(
    val basketId: String,
    @SerializedName("cutleryQuantity") val personsQuantity: Int
)