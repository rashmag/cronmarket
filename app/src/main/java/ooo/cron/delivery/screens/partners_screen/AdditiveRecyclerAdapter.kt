package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.databinding.ItemAdditiveBinding

/*
 * Created by Muhammad on 17.05.2021
 */


class AdditiveRecyclerAdapter(
    private val additives: List<PartnerProductsRes.Additive>
) : RecyclerView.Adapter<AdditiveRecyclerAdapter.ViewHolder>() {

    private var mListener: onDopProductClickListener? = null
    private val checkedAdditives = mutableMapOf<Int, PartnerProductsRes.Additive>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_additive, parent, false)
        )
    }

    fun setListener(listener:onDopProductClickListener){
        this.mListener = listener
    }

    override fun getItemCount() = additives.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAdditive(additives[position], position)
    }

    fun getCheckedAdditives() =
        checkedAdditives.values.toList()

    interface onDopProductClickListener {
        fun setIncreasedPriceDopProduct(increasedPriceDopProduct: String)
        fun setReducePriceDopProduct(reducePriceDopProduct: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemAdditiveBinding.bind(itemView)

        fun bindAdditive(
            additive: PartnerProductsRes.Additive,
            position: Int
        ) {

            var additiveText = additive.name
            if (additive.cost != 0)
                additiveText += " +${additive.cost}₽"

            binding.chkAdditive.text = additiveText

            if (binding.chkAdditive.isChecked) {
                checkedAdditives[position] = additive
            } else {
                checkedAdditives.remove(position)
            }

            binding.chkAdditive.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    mListener?.setIncreasedPriceDopProduct(additive.cost.toString())
                    checkedAdditives[position] = additive
                } else {
                    mListener?.setReducePriceDopProduct(additive.cost.toString())
                    checkedAdditives.remove(position)
                }
            }
        }
    }
}