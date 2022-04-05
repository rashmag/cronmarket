package ooo.cron.delivery.screens.pay_dialog_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.DeliveryTimeItemBinding
import ooo.cron.delivery.utils.extensions.setCustomTextColor

class AdapterDeliveryTime(
    private val getChosenTime: (time: String) -> Unit
) : RecyclerView.Adapter<AdapterDeliveryTime.DeliveryTimeViewHolder>() {

    private val deliveryTimes = arrayListOf<String>()

    var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeliveryTimeViewHolder(
        itemBinding = DeliveryTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DeliveryTimeViewHolder, position: Int) {
        holder.bind(deliveryTimes[position])
    }

    fun setTime(list: List<String>) {
        deliveryTimes.run {
            clear()
            addAll(list)
        }

        notifyItemRangeChanged(0, deliveryTimes.size)
    }

    fun updateSelectedItem(item: Int) {
        selectedItem = item
    }

    override fun getItemCount() = deliveryTimes.size

    inner class DeliveryTimeViewHolder(private val itemBinding: DeliveryTimeItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: String) {

            with(itemBinding) {

                timeText.text = model

                if (selectedItem == bindingAdapterPosition) {
                    timeText.setCustomTextColor(R.color.black)
                    timeText.textSize = MAX_TEXT
                    getChosenTime.invoke(timeText.text.toString())

                } else if (bindingAdapterPosition == selectedItem - 1 || bindingAdapterPosition == selectedItem + 1) {
                    timeText.setCustomTextColor(R.color.grey50)
                    timeText.textSize = MIDDLE_TEXT
                } else {
                    timeText.setCustomTextColor(R.color.grey40)
                    timeText.textSize = SMALL_TEXT
                }
            }
        }
    }

    private companion object{
        private const val SMALL_TEXT = 14f
        private const val MIDDLE_TEXT = 16f
        private const val MAX_TEXT = 24f
    }
}