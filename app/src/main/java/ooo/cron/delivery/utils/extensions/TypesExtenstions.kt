package ooo.cron.delivery.utils.extensions

import android.graphics.Color
import ooo.cron.delivery.R

fun Int.drawableFromStatus() = when(this) {
    1 -> R.drawable.order_status_background_on_moderation
    2 -> R.drawable.order_status_background_on_way
    3 -> R.drawable.order_status_background_preparing
    4 -> R.drawable.order_status_background_delivered
    5 -> R.drawable.order_status_background_cancelled
    else -> ""
}

fun Int.colorFromStatus() = when(this) {
    1 -> Color.parseColor("#000000")
    2,3,4 -> Color.parseColor("#FFFFFF")
    5 -> Color.parseColor("#BF2600")
    else -> 0
}