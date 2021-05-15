package ooo.cron.delivery.screens.partners_screen

import android.graphics.Color
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
                listener.onCategoryClick(position)
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    interface OnCategoryClickListener {
        fun onCategoryClick(position: Int)
    }
}