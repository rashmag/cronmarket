package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_additive, parent, false)
        )
    }

    override fun getItemCount() = additives.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAdditive(additives[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemAdditiveBinding.bind(itemView)

        fun bindAdditive(additive: PartnerProductsRes.Additive) {
            var additiveText = additive.name
            if (additive.cost != 0)
                additiveText += " +${additive.cost}₽"

            binding.tvAdditive.text = additiveText
        }
    }
}