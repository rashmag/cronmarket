package ooo.cron.delivery.screens.order_history_detail_screen.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.OrderHistoryDetailDish
import ooo.cron.delivery.databinding.OrderHistoryDetailItemBinding
import ooo.cron.delivery.utils.extensions.uiLazy

class OrderHistoryDetailAdapter : ListAdapter<OrderHistoryDetailDish, OrderHistoryDetailAdapter.OrderHistoryDetailViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderHistoryDetailViewHolder(
        itemBinding = OrderHistoryDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: OrderHistoryDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderHistoryDetailViewHolder(private val itemBinding: OrderHistoryDetailItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        private val additiveAdapter by uiLazy {
            OrderHistoryDetailAdditiveAdapter()
        }

        fun bind(model: OrderHistoryDetailDish){

            with(itemBinding){

                orderName.text = model.name
                orderQuantity.text = model.quantity.toString()
                orderPrice.text = itemView.context.getString(
                    R.string.order_history_screen_total_amount_ruble,
                    model.cost.toString()
                )

                orderAdditiveRecycler.adapter = additiveAdapter
                additiveAdapter.submitList(model.dishAdditives)

                Glide.with(root)
                    .load(model.photoUri)
                    .centerCrop()
                    .into(orderImage)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderHistoryDetailDish>() {

            override fun areItemsTheSame(oldItem: OrderHistoryDetailDish, newItem: OrderHistoryDetailDish): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: OrderHistoryDetailDish, newItem: OrderHistoryDetailDish): Boolean =
                oldItem.productId == newItem.productId &&
                oldItem.name == newItem.name &&
                        oldItem.quantity == newItem.quantity &&
                        oldItem.cost == newItem.cost &&
                        oldItem.photoUri == newItem.photoUri &&
                        oldItem.dishAdditives == newItem.dishAdditives
        }
    }
}