package ooo.cron.delivery.utils.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.utils.extensions.orZero

class OrderHistoryDetailItemDecoration(
    private val space: Int,
    private val horizontal: Int ?= null
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = space
        outRect.right = horizontal.orZero()
        outRect.left = horizontal.orZero()
        outRect.bottom = space + space

        // Добавление верхнего спэйса только для первого элемента
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space + space
        }
    }
}