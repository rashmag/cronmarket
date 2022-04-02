package ooo.cron.delivery.data.network.models

import com.google.gson.annotations.SerializedName

data class FavoritePartners(
    @SerializedName("partners")
    val partners: List<Partner>
)
