package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerCategoryRes

/*
 * Created by Muhammad on 08.05.2021
 */



class PartnerCategoryAdapter(
    private val categoryRes: PartnerCategoryRes,
    private val listener: OnCategoryClickListener
) :
    RecyclerView.Adapter<PartnerCategoryAdapter.ViewHolder>() {
    private var checkedPosition = 0


    fun setSelected(categoryId: String) {
        categoryRes.categories.indexOfFirst { it.id == categoryId }.also { position ->
            checkedPosition = position
            notifyDataSetChanged()
        }
    }

    fun getCategoryId(position: Int) : String {
        return categoryRes.categories[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_market_category_tag, parent, false)
        )
    }

    override fun getItemCount() = categoryRes.categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            (itemView as TextView).text = categoryRes.categories[position].name
            itemView.setOnClickListener {
                itemView.setBackgroundResource(R.drawable.bg_market_category_tag_selected_item)
                if (checkedPosition != holder.adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = holder.adapterPosition
                }
                listener.onCategoryClick(position)
            }

            if (checkedPosition == -1) {
                itemView.setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
            } else {
                if (checkedPosition == holder.adapterPosition) {
                    itemView.setBackgroundResource(R.drawable.bg_market_category_tag_selected_item)
                } else {
                    itemView.setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
                }
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    interface OnCategoryClickListener {
        fun onCategoryClick(position: Int)
    }
}