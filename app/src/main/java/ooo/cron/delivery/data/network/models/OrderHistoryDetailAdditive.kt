package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

data class OrderHistoryDetailAdditive(
    @SerializedName("Id") val id: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Cost") val cost: Double
)