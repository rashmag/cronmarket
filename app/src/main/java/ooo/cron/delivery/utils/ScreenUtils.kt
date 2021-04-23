package ooo.cron.delivery.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */

fun Resources.dipToPixels(dip: Float) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dip,
    displayMetrics
)