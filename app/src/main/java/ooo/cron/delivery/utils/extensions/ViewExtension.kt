package ooo.cron.delivery.utils.extensions

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.transition.Slide
import androidx.transition.TransitionManager

private const val DEFAULT_ANIMATE_TIME = 300L

fun View.startBottomAnimate(visibility: Boolean, durationTime: Long ?= DEFAULT_ANIMATE_TIME) {
    val transition = Slide(Gravity.BOTTOM)
    transition.apply {
        duration = durationTime ?: 0L
        addTarget(this@startBottomAnimate)
    }
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.isVisible = visibility
}

internal fun View.makeGone() {
    if (!isGone) visibility = View.GONE
}


internal fun View.makeVisible() {
    if (!isVisible) visibility = View.VISIBLE
}


internal fun View.makeInvisible() {
    if (!isInvisible) visibility = View.INVISIBLE
}

fun Int?.orZero(): Int = this ?: 0

fun <T> uiLazy(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}