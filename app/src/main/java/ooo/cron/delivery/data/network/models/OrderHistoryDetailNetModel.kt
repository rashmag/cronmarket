package ooo.cron.delivery.data.network.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class OrderHistoryDetailNetModel(
    @SerializedName("id") val id: String,
    @SerializedName("orderNumber") val orderNumber: String,
    @SerializedName("partnerName") val partnerName: String,
    @SerializedName("totalAmount") val totalAmount: Int,
    @SerializedName("amount") val amount: Int,
    @SerializedName("deliveryCost") val deliveryCost: Int,
    @SerializedName("discount") val discount: Int,
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("statusName") val statusName: String,
    @SerializedName("createDateTime") val dateTime: String,
    @SerializedName("deliverAtTime") val deliverAtTime: String, // TODO: здесь должен быть объект
    @SerializedName("deliveryLocation") val deliveryLocation: String,
    @SerializedName("deliveryCityId") val deliveryCityId: String,
    @SerializedName("orderContent") val orderContent: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("feedbackCreateDateTime") val feedbackCreateDateTime: String,
    @SerializedName("feedbackPositiveText") val feedbackPositiveText: String,
    @SerializedName("feedbackNegativeText") val feedbackNegativeText: String,
    @SerializedName("feedbackModerationStatus") val feedbackModerationStatus: String
){

    companion object {
        fun OrderHistoryDetailNetModel.deserializeDishes() =
            Gson().fromJson(this.orderContent, Array<OrderHistoryDetailDish>::class.java)
                .asList()
    }
}
