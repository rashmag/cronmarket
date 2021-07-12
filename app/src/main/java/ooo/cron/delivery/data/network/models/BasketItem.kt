package ooo.cron.delivery.data.network.models

import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import java.util.*

/**
 * Created by Ramazan Gadzhikadiev on 11.05.2021.
 */
data class BasketItem(
    val productId: UUID,
    val basketProductId: UUID,
    val name: String,
    val quantity: Int,
    val cost: Float,
    val photoUri: Uri
) {

    fun getAmount() =
        cost * quantity

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BasketItem>() {

            override fun areItemsTheSame(
                oldItem: BasketItem,
                newItem: BasketItem
            ): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(
                oldItem: BasketItem,
                newItem: BasketItem
            ): Boolean =
                oldItem == newItem

            override fun getChangePayload(
                oldItem:
                BasketItem, newItem: BasketItem
            ): Any? = Bundle().apply {
                if (oldItem.name != newItem.name)
                    putString(BasketItem::name.name, newItem.name)

                if (oldItem.quantity != newItem.quantity)
                    putInt(BasketItem::quantity.name, newItem.quantity)

                if (oldItem.cost != newItem.cost)
                    putFloat(BasketItem::cost.name, newItem.cost)

                if (oldItem.photoUri != newItem.photoUri)
                    putParcelable(BasketItem::photoUri.name, newItem.photoUri)
            }
        }
    }
}