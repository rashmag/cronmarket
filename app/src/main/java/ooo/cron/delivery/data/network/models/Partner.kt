package ooo.cron.delivery.data.network.models

import androidx.recyclerview.widget.DiffUtil
import java.util.*

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
    val minAmountDelivery: Double?,
    val rating: Float = 0f,
    val feedbackCount: Int = 0,
    val schedule: Schedule
) {
    fun isOpen(): Boolean {
        val currentCalendar = Calendar.getInstance()
        val openCalendar = Calendar.getInstance().apply {
            val openTime = openTime()
            set(Calendar.HOUR_OF_DAY, openTime[0])
            set(Calendar.MINUTE, openTime[1])
        }
        val closeCalendar = Calendar.getInstance().apply {
            val closeTime = closeTime()
            set(Calendar.HOUR_OF_DAY, closeTime[0])
            set(Calendar.MINUTE, closeTime[1])
        }

        return currentCalendar.timeInMillis in
                (openCalendar.timeInMillis..closeCalendar.timeInMillis)
    }

    fun openTime() =
        if (schedule.begin.isNotEmpty())
            schedule.begin.split(':')
                .map { it.toInt() }
        else
            listOf(0, 0, 0)

    fun closeTime() =
        if (schedule.end.isNotEmpty())
            schedule.end.split(':')
                .map { it.toInt() }
        else listOf(23, 59, 59)

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