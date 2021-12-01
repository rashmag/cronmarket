package ooo.cron.delivery.screens.basket_screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.PaintDrawable
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import kotlin.math.abs


/**
 * Created by Ramazan Gadzhikadiev on 17.05.2021.
 */

class SwipeHelper(
    context: Context,
    val onRemoveClicked: (viewHolder: RecyclerView.ViewHolder) -> Unit
) : ItemTouchHelper.Callback() {

    private val backgroundRadius =
        context.resources.getDimension(R.dimen.basket_trash_background_radius)
    private val background = PaintDrawable(ContextCompat.getColor(context, R.color.errors)).apply {
        setCornerRadius(backgroundRadius)
    }

    private val itemTrash = ContextCompat.getDrawable(context, R.drawable.ic_basket_item_trash)
    private val itemTrashMargin = context.resources.getDimension(R.dimen.basket_trash_margin)
        .toInt()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int =
        if (viewHolder is BasketAdapter.ProductViewHolder) {
            makeMovementFlags(0, ItemTouchHelper.LEFT)
        } else
            makeMovementFlags(0, 0)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        if (swipeDirection == Direction.TO_LEFT)
            return 0.01f

        return 0.99f
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d(this::class.simpleName, "Basket item swiped")
    }

    private var delta: Int? = null

    private var swipeDirection = Direction.TO_LEFT
    private var lastDx = 0.0f

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView

            if (delta == null)
                delta = itemView.marginStart + itemView.width / 5

            swipeDirection = if (dX > lastDx || lastDx == 0.0f)
                Direction.TO_RIGHT
            else
                Direction.TO_LEFT

            background.setBounds(
                itemView.right - delta!! -
                        (backgroundRadius.toInt() shl 1),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)
            val itemTrashHeight = itemTrash!!.intrinsicHeight
            val itemTrashTop = itemView.top + (itemView.bottom - itemView.top - itemTrashHeight) / 2

            itemTrash.setBounds(
                itemView.right - itemTrashMargin - itemTrash.intrinsicWidth,
                itemTrashTop,
                itemView.right - itemTrashMargin,
                itemTrashTop + itemTrashHeight
            )
            itemTrash.draw(c)

            var newDx = dX
            if (abs(newDx) >= delta!!) {
                newDx = -delta!!.toFloat()
            }

            recyclerView.setOnTouchListener { v, event ->
                val x = event.x
                val y = event.y
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (x > itemView.right - delta!! &&
                            x < itemView.right &&
                            y > itemView.top &&
                            y < itemView.bottom &&
                            dX < 0.0f
                        ) {
                            onRemoveClicked(viewHolder)
                        }
                        return@setOnTouchListener true
                    }
                    else -> return@setOnTouchListener false
                }
            }

            super.onChildDraw(
                c, recyclerView, viewHolder, newDx, dY,
                actionState, isCurrentlyActive
            )
        }
    }

    enum class Direction {
        TO_RIGHT,
        TO_LEFT
    }
}