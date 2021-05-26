package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Ramazan Gadzhikadiev on 26.05.2021.
 */
data class ProductInBasket(
    @SerializedName("Id")
    val id: String,
    @SerializedName("ProductId")
    val productId: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Quantity")
    val quantity: Int,
    @SerializedName("Cost")
    val cost: Double,
    @SerializedName("PhotoUri")
    val photoUri: String,
    @SerializedName("Additives")
    val additives: List<AdditiveInBasket>
)