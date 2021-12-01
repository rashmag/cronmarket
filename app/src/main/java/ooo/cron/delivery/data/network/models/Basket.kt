package ooo.cron.delivery.data.network.models

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ramazan Gadzhikadiev on 20.05.2021.
 */

@Parcelize
data class Basket(
    val id: String,
    val marketCategoryId: Int,
    val partnerId: String,
    val amount: Double,
    val deliveryCost: Double,
    val cutleryCount: Int,
    val content: String
): Parcelable {
    companion object {
        fun Basket.deserializeDishes() =
            Gson().fromJson(this.content, Array<BasketDish>::class.java)
                .asList()
    }
}