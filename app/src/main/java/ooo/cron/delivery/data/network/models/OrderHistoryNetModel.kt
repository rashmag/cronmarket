package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

data class OrderHistoryNetModel(
    @SerializedName("id") val id: String,
    @SerializedName("orderNumber") val orderNumber: String,
    @SerializedName("partnerName") val partnerName: String,
    @SerializedName("totalAmount") val totalAmount: Int,
    @SerializedName("status") val status: String,
    @SerializedName("createDateTime") val dateTime: String,
    @SerializedName("deliveryLocation") val deliveryLocation: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("deliveryCityId") val deliveryCityId: String
)