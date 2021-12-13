package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerCategoryRes
import ooo.cron.delivery.databinding.ItemMarketCategoryTagBinding

/*
 * Created by Muhammad on 08.05.2021
 */

class PartnerCategoryAdapter(
    private val categoryRes: PartnerCategoryRes,
    private val onCategoryClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PartnerCategoryAdapter.PartnerCategoryViewHolder>() {

    private var checkedPosition = 0

    fun setSelected(position: Int){
        notifyItemChanged(checkedPosition, Unit)
        checkedPosition = position
        notifyItemChanged(position, Unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerCategoryViewHolder =
        PartnerCategoryViewHolder(
            itemBinding = ItemMarketCategoryTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = categoryRes.categories.size

    override fun onBindViewHolder(holder: PartnerCategoryViewHolder, position: Int) {
        holder.bind()
    }

    inner class PartnerCategoryViewHolder(private val itemBinding: ItemMarketCategoryTagBinding): RecyclerView.ViewHolder(itemBinding.root){

        fun bind(){
            itemBinding.tagTitle.text = categoryRes.categories[bindingAdapterPosition].name

            if(checkedPosition == -1) {
                itemView.setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
            }else{
                if(checkedPosition == bindingAdapterPosition) {
                    itemView.setBackgroundResource(R.drawable.bg_market_category_tag_selected_item)
                }else{
                    itemView.setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
                }
            }

            itemView.setOnClickListener{
                itemView.setBackgroundResource(R.drawable.bg_market_category_tag_selected_item)
                if(checkedPosition != bindingAdapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = bindingAdapterPosition
                }

                onCategoryClick.invoke(bindingAdapterPosition)
            }
        }
    }
}