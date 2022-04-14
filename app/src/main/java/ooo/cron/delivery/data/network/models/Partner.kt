package ooo.cron.delivery.data.network.models

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.text.SimpleDateFormat
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
    val schedule: Schedule,
    @SerializedName("isFavorite") val isFavorite: Boolean
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
        if (openCalendar.timeInMillis > closeCalendar.timeInMillis
            && (currentCalendar.timeInMillis < closeCalendar.timeInMillis
                    || currentCalendar.timeInMillis > openCalendar.timeInMillis)
        ) {
            return true
        }
        return currentCalendar.timeInMillis in
                (openCalendar.timeInMillis..closeCalendar.timeInMillis)
    }

    @SuppressLint("SimpleDateFormat")
    fun openTime() = if (schedule.begin.isNotEmpty())
        schedule.begin.split(':')
            .map { it.toInt() }
    else listOf(0, 0, 0)


    fun closeTime(): List<Int> {
        if (schedule.end.isNotEmpty() && name.equals("KFC")) {
            return minusThirtyMinuteForKFC()
        } else if (schedule.end.isNotEmpty()) {
            val close = schedule.end.split(':')
                .map { it.toInt() }
            return close
        } else {
            return listOf(23, 59, 59)
        }
    }

    fun minusThirtyMinuteForKFC(): List<Int> {
        val close = schedule.end.split(':')
            .map { it.toInt() }
        val closeCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, close[0])
            set(Calendar.MINUTE, close[1])
        }

        val date = Date(closeCalendar.timeInMillis - THIRTY_MINUTES)
        val formatter: DateFormat = SimpleDateFormat("HH:mm")
        val dateFormatted: String = formatter.format(date)

        val endClose = dateFormatted.split(':')
            .map { it.toInt() }
        return endClose
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Partner>() {
            override fun areItemsTheSame(oldItem: Partner, newItem: Partner): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Partner, newItem: Partner): Boolean {
                return oldItem.id == newItem.id
            }
        }

        const val THIRTY_MINUTES = 1800000
    }
}