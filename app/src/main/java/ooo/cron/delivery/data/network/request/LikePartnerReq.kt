package ooo.cron.delivery.data.network.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Maya Nasrueva on 02.04.2021
 * */

data class LikePartnerReq (
    @SerializedName("partnerId")
    val partnerId: String
)