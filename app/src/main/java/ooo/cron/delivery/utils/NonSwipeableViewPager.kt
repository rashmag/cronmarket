package ooo.cron.delivery.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

/*
 * Created by Muhammad on 28.04.2021
 */



class NonSwipeableViewPager : ViewPager {
    constructor(context: Context?) : super(context!!) {
        setMyScroller()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        setMyScroller()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // запрет на свайп
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // запрет на свайп
        return false
    }

    private fun setMyScroller() {
        try {
            val viewpager: Class<*> = ViewPager::class.java
            val scroller: Field = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MyScroller(context))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class MyScroller(context: Context?) : Scroller(context, DecelerateInterpolator()) {
        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/)
        }
    }
}