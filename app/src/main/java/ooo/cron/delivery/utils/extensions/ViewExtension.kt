package ooo.cron.delivery.utils.extensions

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import androidx.transition.TransitionManager
import ooo.cron.delivery.R

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

@RequiresApi(Build.VERSION_CODES.M)
fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    val drawable = ContextCompat.getDrawable(context, resourceId)
    foreground = drawable
}

inline fun <T : Fragment> T.withArgs(
    argsBuilder: Bundle.() -> Unit
): T =
    this.apply {
        arguments = Bundle().apply(argsBuilder)
    }

@SuppressLint("ResourceAsColor")
internal fun TextView.setBold(){
    setTypeface(this.typeface, Typeface.BOLD)
    setTextColor(Color.parseColor("#000000"))
}

fun ImageView.setActiveIndicator(){
    setBackgroundResource(R.drawable.circle_indicator_active)
    setImageResource(R.drawable.ic_waiting)
}

fun ImageView.setDoneIndicator(){
    setBackgroundResource(R.drawable.circle_indicator_done)
    setImageResource(R.drawable.ic_done_10dp)
}

fun ProgressBar.setDoneColor(){
    progressTintList = ColorStateList.valueOf(Color.parseColor("#00875A"))
}

fun TextView.setDrawableStart(icon: Int){
    setCompoundDrawablesWithIntrinsicBounds(
        icon,
        0,
        0,
        0
    )
}

internal fun View.requestNewSize(
    width: Int = layoutParams.width,
    height: Int = layoutParams.height
) {
    layoutParams.width = width
    layoutParams.height = height
    layoutParams = layoutParams
}

fun ImageView.setTint(color: Int){
    DrawableCompat.setTint(
        DrawableCompat.wrap(this.drawable),
        ContextCompat.getColor(context, color)
    )
}