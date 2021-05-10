package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.databinding.ItemPartnerProductBinding

/*
 * Created by Muhammad on 08.05.2021
 */



class PartnerProductAdapter(private val productList: List<PartnerProductsRes>) :
    RecyclerView.Adapter<PartnerProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_partner_product, parent, false)
        )
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemPartnerProductBinding.bind(itemView)

        fun bind(product: PartnerProductsRes) {
            binding.run {
                with(product) {
                    tvProductName.text = name
                    tvCost.text = cost.toString()
                    tvGram.text = portionSize

                    Glide.with(root)
                        .load(photo)
                        .into(ivProduct)
                }
            }
        }
    }
}