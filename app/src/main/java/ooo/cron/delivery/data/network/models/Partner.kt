package ooo.cron.delivery.data.network.models

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
data class Partner(
    val id: String,
    val name: String,
    val fullDescription: String,
    val shortDescription: String,
    val logo: String,
    val mainWinImg: String,
    val partnerCardImg: String,
    val minAmountOrder: Double,
    val minAmountDelivery: Double,
    val rating: Float,
    val feedbackCount: Int
) {
    companion object {
         val DIFF_CALBACK = object : DiffUtil.ItemCallback<Partner>() {
             override fun areItemsTheSame(oldItem: Partner, newItem: Partner): Boolean {
                 return oldItem.id == newItem.id
             }

             override fun areContentsTheSame(oldItem: Partner, newItem: Partner): Boolean {
                 return oldItem.id == newItem.id
             }
         }
    }
}