package ooo.cron.delivery.screens.pay_dialog_screen

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.DeliveryTimeItemBinding

class AdapterDeliveryTime(
    private val getChosenTime:(time: String) -> Unit
) : RecyclerView.Adapter<AdapterDeliveryTime.DeliveryTimeViewHolder>() {

    private val deliveryTimes = arrayListOf<String>()

    var parentItem = 1
    var timeSelected = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeliveryTimeViewHolder(
        itemBinding =  DeliveryTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DeliveryTimeViewHolder, position: Int) {
        holder.bind(deliveryTimes[position])
    }

    fun setTime(list: List<String>){
        deliveryTimes.run {
            clear()
            addAll(list)
        }

        notifyDataSetChanged()
    }

    override fun getItemCount() = deliveryTimes.size

    inner class DeliveryTimeViewHolder(private val itemBinding: DeliveryTimeItemBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(model: String){

            with(itemBinding){

                timeText.text = model

                if (parentItem == bindingAdapterPosition) {
                    timeText.setTextColor(
                        itemView.context.resources.getColor(
                            R.color.black
                        )
                    )

                    timeSelected = deliveryTimes[bindingAdapterPosition].replace(" ", "")

                    timeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)

                    getChosenTime.invoke(timeText.text.toString())

                } else if (bindingAdapterPosition == parentItem - 1 || bindingAdapterPosition == parentItem + 1) {
                    timeText.setTextColor(
                        itemView.context.resources.getColor(
                            R.color.grey50
                        )
                    )
                    timeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                } else {
                    timeText.setTextColor(
                        itemView.context.resources.getColor(
                            R.color.grey40
                        )
                    )
                    timeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                }

                timeText.text = deliveryTimes[bindingAdapterPosition]
            }
        }
    }
}