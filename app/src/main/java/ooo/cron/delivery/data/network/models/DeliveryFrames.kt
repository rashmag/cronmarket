package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

data class DeliveryFrames(
    @SerializedName("deliveryTypeId") val deliveryTypeId: Int,
    @SerializedName("deliveryTypeName") val deliveryTypeName: String,
    @SerializedName("deliveryCosts") val deliveryCosts: List<DeliveryCost>
)