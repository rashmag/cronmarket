package ooo.cron.delivery.screens.order_history_screen.presentation

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import ooo.cron.delivery.databinding.OrderHistoryItemBinding
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryAdapter.OrderHistoryViewHolder
import ooo.cron.delivery.utils.extensions.addRipple
import ooo.cron.delivery.utils.extensions.colorFromStatus
import ooo.cron.delivery.utils.extensions.drawableFromStatus
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(
    private val onOrderClick: (orderId: String) -> Unit
) : ListAdapter<OrderHistoryNetModel, OrderHistoryViewHolder>(DIFF_CALLBACK) {

    private var statusNumber = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderHistoryViewHolder(
        itemBinding = OrderHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderHistoryViewHolder(
        private val itemBinding: OrderHistoryItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: OrderHistoryNetModel){

            with(itemBinding){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cardViewOrder.addRipple()
                }
                orderNumber.text = model.orderNumber
                partnerName.text = model.partnerName

                val input = SimpleDateFormat(BACKEND_DATE_FORMAT)
                val output = SimpleDateFormat(NECESSARY_FORMAT)
                val date = input.parse(model.dateTime)
                orderDate.text = output.format(date)

                orderAddress.text = model.deliveryLocation
                orderStatus.text = model.status
                when(model.status){
                    MODERATION -> statusNumber = 1
                    ON_WAY -> statusNumber = 2
                    IN_PROCESS -> statusNumber = 3
                    DONE -> statusNumber = 4
                    CANCELLED -> statusNumber = 5
                    else -> 0
                }
                orderStatus.setBackgroundResource(statusNumber.drawableFromStatus() as? Int ?: 0)
                orderStatus.setTextColor(statusNumber.colorFromStatus())
                orderPrice.text = itemView.context.getString(
                    R.string.order_history_screen_total_amount_ruble,
                    model.totalAmount.toString()
                )

                itemView.setOnClickListener {
                    onOrderClick.invoke(model.id)
                }
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderHistoryNetModel>() {

            override fun areItemsTheSame(oldItem: OrderHistoryNetModel, newItem: OrderHistoryNetModel): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: OrderHistoryNetModel, newItem: OrderHistoryNetModel): Boolean =
                oldItem.id == newItem.id &&
                oldItem.orderNumber == newItem.orderNumber &&
                        oldItem.partnerName == newItem.partnerName &&
                        oldItem.totalAmount == newItem.totalAmount &&
                        oldItem.status == newItem.status &&
                        oldItem.dateTime == newItem.dateTime &&
                        oldItem.deliveryLocation == newItem.deliveryLocation &&
                        oldItem.rating == newItem.rating &&
                        oldItem.deliveryCityId == newItem.deliveryCityId
        }

        private const val MODERATION = "Модерация"
        private const val ON_WAY = "В пути"
        private const val IN_PROCESS = "Готовится"
        private const val DONE = "Доставлен"
        private const val CANCELLED = "Отменен"

        private const val BACKEND_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        private const val NECESSARY_FORMAT = "dd.MM.yy HH:mm"
    }
}