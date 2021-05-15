package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.ProductCategoryModel
import ooo.cron.delivery.databinding.ItemPartnerProductBinding
import ooo.cron.delivery.utils.section_recycler_view.SectionRecyclerViewAdapter
import ooo.cron.delivery.utils.section_recycler_view.SectionRecyclerViewHolder

/*
 * Created by Muhammad on 08.05.2021
 */



class CategoryAdapter(private val productCategoryModel: List<PartnerProductsRes>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun getItemCount() = productCategoryModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(productCategoryModel[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_partner_product,
                parent,
                false
            )
        )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemPartnerProductBinding.bind(view)

        fun bindProduct(product: PartnerProductsRes) {
            binding.run {
                with(product) {
                    tvProductName.text = name
                    tvCost.text = cost.toString()
                    tvGram.text = portionSize

                    com.bumptech.glide.Glide.with(root)
                        .load(photo)
                        .into(ivProduct)
                }
            }
        }

    }

}

class PartnerProductAdapter(private val productCategoryModel: List<ProductCategoryModel>) :
    SectionRecyclerViewAdapter<PartnerProductAdapter.ViewHolder, ProductCategoryModel>(
        productCategoryModel
    ) {

    override fun viewHolder(view: View) = ViewHolder(view)

    private lateinit var currentRecyclerView: RecyclerView

    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            currentRecyclerView = recyclerView
            recyclerView.adapter = CategoryAdapter(productCategoryModel[position].productList)
        }

    }

    fun getRecyclerView(): RecyclerView {
        return currentRecyclerView
    }

}
