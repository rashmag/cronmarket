package ooo.cron.delivery.screens.partners_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.DeliveryCost
import ooo.cron.delivery.databinding.DeliveryTypePriceItemBinding

class DeliveryTypePriceAdapter :
    ListAdapter<DeliveryCost, DeliveryTypePriceAdapter.DeliveryTypePriceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeliveryTypePriceViewHolder(
        itemBinding = DeliveryTypePriceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: DeliveryTypePriceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DeliveryTypePriceViewHolder(private val itemBinding: DeliveryTypePriceItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("StringFormatMatches")
        fun bind(model: DeliveryCost){

            with(itemBinding){

                if(model.amountTo != EMPTY){
                    deliveryFromTitle.text = itemView.context.getString(
                        R.string.partners_screen_delivery_from_title_second,
                        model.amountFrom,
                        model.amountTo
                    )
                }else{
                    deliveryFromTitle.text = itemView.context.getString(
                        R.string.partners_screen_delivery_from_title,
                        model.amountFrom
                    )
                }

                deliveryTypePrice.text = itemView.context.getString(
                    R.string.partners_screen_delivery_type_price,
                    model.deliveryCost
                )
            }
        }
    }

    private companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DeliveryCost>() {

            override fun areItemsTheSame(oldItem: DeliveryCost, newItem: DeliveryCost): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: DeliveryCost, newItem: DeliveryCost): Boolean =
                oldItem.amountFrom == newItem.amountFrom &&
                        oldItem.amountTo == newItem.amountTo &&
                        oldItem.deliveryCost == newItem.deliveryCost
        }

        private const val EMPTY = 0
    }
}