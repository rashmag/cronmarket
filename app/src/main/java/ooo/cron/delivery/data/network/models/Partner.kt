package ooo.cron.delivery.data.network.models

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
data class Partner(
    val id: String = "",
    val name: String = "",
    val fullDescription: String = "",
    val shortDescription: String = "",
    val logo: String = "",
    val mainWinImg: String = "",
    val partnerCardImg: String = "",
    val minAmountOrder: Double = 0.0,
    val minAmountDelivery: Double = 0.0,
    val rating: Float = 0f,
    val feedbackCount: Int = 0
) {
    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Partner>() {
             override fun areItemsTheSame(oldItem: Partner, newItem: Partner): Boolean {
                 return oldItem.id == newItem.id
             }

             override fun areContentsTheSame(oldItem: Partner, newItem: Partner): Boolean {
                 return oldItem.id == newItem.id
             }
         }
    }
}