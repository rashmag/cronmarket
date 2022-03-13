package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

data class DeliveryCost(
    @SerializedName("amountFrom") val amountFrom: Int,
    @SerializedName("amountTo") val amountTo: Int,
    @SerializedName("deliveryCost") val deliveryCost: Int
)