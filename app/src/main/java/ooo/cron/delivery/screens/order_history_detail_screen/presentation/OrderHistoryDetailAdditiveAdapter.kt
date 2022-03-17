package ooo.cron.delivery.screens.order_history_detail_screen.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.OrderHistoryDetailAdditive
import ooo.cron.delivery.databinding.OrderAdditiveItemBinding

class OrderHistoryDetailAdditiveAdapter :
    ListAdapter<OrderHistoryDetailAdditive, OrderHistoryDetailAdditiveAdapter.OrderAdditiveViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderAdditiveViewHolder(
        itemBinding = OrderAdditiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: OrderAdditiveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderAdditiveViewHolder(val itemBinding: OrderAdditiveItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: OrderHistoryDetailAdditive){

            with(itemBinding){

                additiveName.text = itemView.context.getString(
                    R.string.order_history_detail_screen_additive_plus,
                    model.name
                )
                additivePrice.text = model.cost.toString()
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderHistoryDetailAdditive>() {

            override fun areItemsTheSame(oldItem: OrderHistoryDetailAdditive, newItem: OrderHistoryDetailAdditive): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: OrderHistoryDetailAdditive, newItem: OrderHistoryDetailAdditive): Boolean =
                oldItem.name == newItem.name && oldItem.cost == newItem.cost
        }
    }
}