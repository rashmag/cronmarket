package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

data class OrderHistoryDetailDish(
    @SerializedName("Id")
    val id: String,
    @SerializedName("ProductId")
    val productId: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Quantity")
    val quantity: Int,
    @SerializedName("Cost")
    val cost: Int,
    @SerializedName("PhotoUri")
    val photoUri: String,
    @SerializedName("Additives")
    val dishAdditives: List<OrderHistoryDetailAdditive>
)