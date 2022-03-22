package ooo.cron.delivery.screens.partners_screen.bottom_sheet_dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.RequireAdditiveModel
import ooo.cron.delivery.databinding.ItemRequireAdditivesBinding
import android.text.SpannableString

import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import ooo.cron.delivery.utils.itemdecoration.LineItemDicoration
import ooo.cron.delivery.utils.additive_section_recycler_view.AdapterAdditiveSectionRecyclerView
import ooo.cron.delivery.utils.additive_section_recycler_view.HolderAdditiveSectionRecyclerView


/*
 * Created by Muhammad on 29.05.2021
 */



class RequireAdditivesAdapter(
    private val requireAdditivesList: ArrayList<RequireAdditiveModel>,
    private val listener: AdditivesAdapter.OnRequireAdditivesListener
) : AdapterAdditiveSectionRecyclerView<RequireAdditivesAdapter.ViewHolderSectionRecyclerViewHolderAdditiveSectionRecyclerView, RequireAdditiveModel>(
) {

    private val checkedAdditives = mutableMapOf<Int, PartnerProductsRes.Additive>()

    fun getCheckedAdditives() =
        checkedAdditives.values.toList()

    override fun viewHolder(view: View) = ViewHolderSectionRecyclerViewHolderAdditiveSectionRecyclerView(view)

    inner class ViewHolderSectionRecyclerViewHolderAdditiveSectionRecyclerView(view: View) : HolderAdditiveSectionRecyclerView(view) {

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

    override fun getSectionList(): ArrayList<RequireAdditiveModel> {
        return requireAdditivesList
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
                val context = rbAdditives.context
                val nameAndPrice = item.name + context.getString(
                    R.string.partner_product_additive_space_price, item.cost
                )
                rbAdditives.apply {
                    text = setAnotherColorForPrice(nameAndPrice,context, item.name)
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
        private fun setAnotherColorForPrice(nameAndPrice:String,
                                            context: Context,
                                            name:String):Spannable {
            val nameAdditive = SpannableString(nameAndPrice)
            nameAdditive.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.priceAdditive)), name.length,
                nameAndPrice.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return nameAdditive
        }
    }

    interface OnRequireAdditivesListener
}

