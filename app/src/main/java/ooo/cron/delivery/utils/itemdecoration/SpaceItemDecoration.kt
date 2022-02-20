package ooo.cron.delivery.utils.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import ooo.cron.delivery.utils.extensions.orZero

class SpaceItemDecoration(
    private val space: Int,
    private val left: Int ?= null,
    private val right: Int ?= null
) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = right.orZero()
        outRect.left = left.orZero()

        // Добавление верхнего спэйса только для первого элемента
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }
    }
}