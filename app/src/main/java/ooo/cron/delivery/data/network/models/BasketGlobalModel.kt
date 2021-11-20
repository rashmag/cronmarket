package ooo.cron.delivery.data.network.models

import com.google.gson.Gson

data class BasketGlobalModel(
    val id: String = "",
    val marketCategoryId: Int = 0,
    val partnerId: String = "",
    val amount: Double = 0.0,
    val deliveryCost: Double = 0.0,
    val cutleryCount: Int = 0,
    val content: String = ""
) {
    companion object {
        fun BasketGlobalModel.deserializeDishes() =
            Gson().fromJson(this.content, Array<BasketDish>::class.java)
                .asList()
    }
}