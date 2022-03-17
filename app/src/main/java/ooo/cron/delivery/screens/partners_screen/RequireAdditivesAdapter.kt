package ooo.cron.delivery.screens.partners_screen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.github.fajaragungpramana.sectionrecyclerview.SectionRecyclerViewAdapter
import com.github.fajaragungpramana.sectionrecyclerview.SectionRecyclerViewHolder
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.RequireAdditiveModel
import ooo.cron.delivery.databinding.ItemRequireAdditivesBinding
import android.text.SpannableString

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import ooo.cron.delivery.utils.itemdecoration.LineItemDicoration


/*
 * Created by Muhammad on 29.05.2021
 */



class RequireAdditivesAdapter(
    private val requireAdditivesList: ArrayList<RequireAdditiveModel>,
    private val listener: AdditivesAdapter.OnRequireAdditivesListener
) : SectionRecyclerViewAdapter<RequireAdditivesAdapter.ViewHolder, RequireAdditiveModel>(
    requireAdditivesList
) {

    private val checkedAdditives = mutableMapOf<Int, PartnerProductsRes.Additive>()

    fun getCheckedAdditives() =
        checkedAdditives.values.toList()

    override fun viewHolder(view: View) = ViewHolder(view)

    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            recyclerView.adapter =
                AdditivesAdapter(requireAdditivesList[position].additiveList, listener) {
                    checkedAdditives[position] = it
                }
            recyclerView.addItemDecoration(
                LineItemDicoration(
                    ContextCompat.getDrawable(
                        recyclerView.context,
                        R.drawable.line_gray
                    )!!
                )
            )
        }

    }

}


class AdditivesAdapter(
    private val requireAdditivesList: List<PartnerProductsRes.Additive>,
    private val listener: OnRequireAdditivesListener,
    private val onChecked: (additive: PartnerProductsRes.Additive) -> Unit
) : RecyclerView.Adapter<AdditivesAdapter.ViewHolder>() {

    private var checkedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_require_additives, parent, false)
        )
    }

    override fun getItemCount() = requireAdditivesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(requireAdditivesList[position])
        holder.onCheck()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRequireAdditivesBinding.bind(itemView)

        fun onCheck(){
            onChecked(requireAdditivesList[checkedPosition])
        }
        fun bind(item:PartnerProductsRes.Additive){
            with(binding) {
                costAdditiveTV.text = costAdditiveTV.context.getString(
                    ooo.cron.delivery.R.string.partner_product_additive_space_price, item.cost
                )
                rbAdditives.apply {
                    text = item.name
                    isChecked = adapterPosition == checkedPosition
                    setOnClickListener {
                        if (checkedPosition != adapterPosition) {
                            notifyItemChanged(checkedPosition)
                            checkedPosition = adapterPosition
                        }
                    }
                }
            }
        }
    }

    interface OnRequireAdditivesListener
}

