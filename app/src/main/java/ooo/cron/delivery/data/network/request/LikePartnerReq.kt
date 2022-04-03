package ooo.cron.delivery.data.network.request

import com.google.gson.annotations.SerializedName

data class LikePartnerReq (
    @SerializedName("partnerId")
    val partnerId: String
)