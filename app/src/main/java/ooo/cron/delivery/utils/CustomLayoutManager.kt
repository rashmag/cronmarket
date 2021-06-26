package ooo.cron.delivery.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/*
 * Created by Muhammad on 25.06.2021
 */



open class CustomLayoutManager(context: Context) : LinearLayoutManager(context) {
    private var isScrollEnabled = true;


    public fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag;
    }


    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}