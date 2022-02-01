package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.databinding.ItemPartnerProductBinding
import ooo.cron.delivery.utils.BasketCounterTimer
import ooo.cron.delivery.utils.section_recycler_view.SectionRecyclerViewAdapter
import ooo.cron.delivery.utils.section_recycler_view.SectionRecyclerViewHolder

/*
 * Created by Muhammad on 08.05.2021
 */



class CategoryAdapter(
    private val productCategoryModel: List<PartnerProductsRes>,
    private val listener: OnProductClickListener,
    private val isOpen: Boolean
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun getItemCount() = productCategoryModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_partner_product,
                parent,
                false
            )
        )

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemPartnerProductBinding.bind(view)

        init {
            if(isOpen) {
                view.setOnClickListener {
                    listener.onProductClick(productCategoryModel[bindingAdapterPosition])
                }

                binding.tvCost.setOnClickListener {
                    val product = productCategoryModel[bindingAdapterPosition]
                    if (product.additives.isEmpty() &&
                            product.requiredAdditiveGroups.isEmpty()
                    ) {
                        val currentQuantity = binding.tvPortionCount.text.toString().toInt() + 1

                        when (defineQuantityChangeStatus(currentQuantity, product.inBasketQuantity)) {
                            QuantityChangeStatus.INCREASED -> increaseProduct(product, currentQuantity)
                            QuantityChangeStatus.NO_CHANGES -> updateCounter(currentQuantity)
                            else -> {
                            }
                        }
                    } else {
                        listener.onProductClick(product)
                    }
                }
            }

            binding.ivPlus.setOnClickListener {
                val product = productCategoryModel[bindingAdapterPosition]
                if (product.additives.isEmpty() &&
                    product.requiredAdditiveGroups.isEmpty()
                ) {
                    val currentQuantity = binding.tvPortionCount.text.toString().toInt() + 1
                    updateCounter(currentQuantity)

                    when(defineQuantityChangeStatus(currentQuantity, product.inBasketQuantity)){
                        QuantityChangeStatus.INCREASED -> increaseProduct(product, currentQuantity)
                        QuantityChangeStatus.DECREASED -> listener.onMinusClick(
                                product,
                                product.inBasketQuantity - currentQuantity
                            )
                        QuantityChangeStatus.NO_CHANGES -> updateCounter(currentQuantity)
                    }
                }
                else {
                    listener.onProductClick(product)
                }
            }

            binding.ivMinus.setOnClickListener {
                val product = productCategoryModel[bindingAdapterPosition]
                val currentQuantity = binding.tvPortionCount.text.toString().toInt() - 1
                updateCounter(currentQuantity)

                when(defineQuantityChangeStatus(currentQuantity, product.inBasketQuantity)){
                    QuantityChangeStatus.INCREASED -> increaseProduct(product, currentQuantity)
                    QuantityChangeStatus.DECREASED -> listener.onMinusClick(product, product.inBasketQuantity - currentQuantity)
                    QuantityChangeStatus.NO_CHANGES -> updateCounter(currentQuantity)
                }
            }
        }

        fun bindProduct(position: Int) {
            val product = productCategoryModel[position]
            binding.run {
                with(product) {
                    tvProductName.text = name
                    tvCost.text = "$cost ₽"
                    tvGram.text = portionSize

                    updateCounter(inBasketQuantity)

                    com.bumptech.glide.Glide.with(root)
                        .load(photo)
                        .placeholder(R.drawable.bg_basket_product_content)
                        .into(ivProduct)
                }
            }
        }

        private fun updateCounter(quantity: Int) {
            binding.tvPortionCount.text = quantity.toString()
            if (quantity <= 0 || !isOpen) {
                binding.tvCost.visibility = View.VISIBLE
                binding.vgAddProduct.visibility = View.INVISIBLE
                return
            }
            binding.tvCost.visibility = View.INVISIBLE
            binding.vgAddProduct.visibility = View.VISIBLE
        }

        private fun defineQuantityChangeStatus(
            quantity: Int,
            inBasketQuantity: Int
        ): QuantityChangeStatus {
            if (quantity > inBasketQuantity)
                return QuantityChangeStatus.INCREASED

            if (quantity < inBasketQuantity)
                return QuantityChangeStatus.DECREASED

            return QuantityChangeStatus.NO_CHANGES
        }

        private fun increaseProduct(product: PartnerProductsRes, currentQuantity: Int) {
            if (product.additives.isEmpty() &&
                product.requiredAdditiveGroups.isEmpty()
            )
                return listener.onPlusClick(
                        product,
                        listOf(),
                        currentQuantity - product.inBasketQuantity
                    )
            listener.onProductClick(product)
        }
    }

    private enum class QuantityChangeStatus {
        INCREASED, DECREASED, NO_CHANGES
    }

    interface OnProductClickListener {
        fun onProductClick(product: PartnerProductsRes)
        fun onPlusClick(
            product: PartnerProductsRes,
            additives: List<BasketDishAdditive>,
            quantity: Int
        )

        fun onMinusClick(product: PartnerProductsRes, quantity: Int)
    }

}

class PartnerProductAdapter(private val isOpen: Boolean) :
    SectionRecyclerViewAdapter<PartnerProductAdapter.ViewHolder, ProductCategoryModel>() {

    private var productCategoryModel = arrayListOf<ProductCategoryModel>()
    private lateinit var listener: CategoryAdapter.OnProductClickListener

    fun setSectionData(productCategoryModel: List<ProductCategoryModel>) {
        this.productCategoryModel.run {
            clear()
            addAll(productCategoryModel)
        }
        notifyDataSetChanged()
    }

    fun setProductClickListener(listener: CategoryAdapter.OnProductClickListener) {
        this.listener = listener
    }

    override fun getSectionList(): ArrayList<ProductCategoryModel> = productCategoryModel

    override fun viewHolder(view: View) = ViewHolder(view)

    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            recyclerView.adapter =
                CategoryAdapter(productCategoryModel[position].productList, listener, isOpen)
        }
    }
}
