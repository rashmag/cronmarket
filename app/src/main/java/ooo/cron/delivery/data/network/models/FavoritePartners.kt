package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Maya Nasrueva on 02.04.2021
 * */

data class FavoritePartners(
    @SerializedName("partners") val partners: List<Partner>
)
