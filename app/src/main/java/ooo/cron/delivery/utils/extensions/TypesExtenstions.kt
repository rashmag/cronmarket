package ooo.cron.delivery.utils.extensions

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
    1 -> R.color.black
    2,3,4 -> R.color.white
    5 -> R.color.red_bf2600
    else -> 0
}