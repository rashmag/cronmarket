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



class CategoryAdapter(
    private val productCategoryModel: List<PartnerProductsRes>,
    private val listener: OnProductClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun getItemCount() = productCategoryModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(position)
        holder.itemView.setOnClickListener {
            listener.onProductClick(productCategoryModel[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_partner_product,
                parent,
                false
            )
        )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemPartnerProductBinding.bind(view)

        fun bindProduct(position: Int) {
            val product = productCategoryModel[position]
            binding.run {
                with(product) {
                    tvProductName.text = name
                    tvCost.text = cost.toString()
                    tvGram.text = portionSize

                    showPriceAndCounter(this)

                    com.bumptech.glide.Glide.with(root)
                        .load(photo)
                        .into(ivProduct)
                }
            }

            binding.tvCost.setOnClickListener{
                listener.onPriceClick(product, position)
            }

            binding.ivPlus.setOnClickListener{
                listener.onPlusClick(product, position)
            }

            binding.ivMinus.setOnClickListener{
                listener.onMinusClick(product, position)
            }
        }

        private fun showPriceAndCounter(product: PartnerProductsRes) {
            if (product.inBasketQuantity <= 0) {
                binding.vgCost.visibility = View.VISIBLE
                return
            }

            binding.tvCost.visibility = View.INVISIBLE
            binding.vgAddProduct.visibility = View.VISIBLE
            binding.tvPortionCount.text = product.inBasketQuantity.toString()
        }
    }

    interface OnProductClickListener {
        fun onProductClick(product: PartnerProductsRes)
        fun onPriceClick(product: PartnerProductsRes, position: Int)
        fun onPlusClick(product: PartnerProductsRes, position: Int)
        fun onMinusClick(product: PartnerProductsRes, position: Int)
    }

}

class PartnerProductAdapter(
    private val productCategoryModel: List<ProductCategoryModel>,
    private val listener: CategoryAdapter.OnProductClickListener
) :
    SectionRecyclerViewAdapter<PartnerProductAdapter.ViewHolder, ProductCategoryModel>(
        productCategoryModel
    ) {

    override fun viewHolder(view: View) = ViewHolder(view)


    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            recyclerView.adapter =
                CategoryAdapter(productCategoryModel[position].productList, listener)
        }
    }
}
