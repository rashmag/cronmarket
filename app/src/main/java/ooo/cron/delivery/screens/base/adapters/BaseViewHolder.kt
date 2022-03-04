package ooo.cron.delivery.screens.base.adapters

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var itemHeight: Int = 0
    protected var currentItem: T? = null

    open fun bind(item: T) {
        this.currentItem = item
    }

    open fun resetHolder(id: String?, fullReset: Boolean = false) {}

    open fun showHolder() {

        if (itemView.isVisible) return

        val params = itemView.layoutParams
        params.height = itemHeight

        itemView.layoutParams = params
        itemView.makeVisible()
    }

    open fun hideHolder() {

        val params = itemView.layoutParams
        if (itemView.measuredHeight != 0)
            itemHeight = itemView.measuredHeight

        params.height = 0

        itemView.layoutParams = params
        itemView.makeGone()
    }
}
