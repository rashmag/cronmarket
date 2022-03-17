package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.databinding.ItemPartnerProductBinding
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeInvisible
import ooo.cron.delivery.utils.extensions.makeVisible
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

    private var quantityCount = 0

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
            with(binding) {

                if (isOpen) {
                    ivProduct.setOnClickListener {
                        listener.onProductClick(productCategoryModel[bindingAdapterPosition])
                    }

                    containerAddToBasket.setOnClickListener {
                        val product = productCategoryModel[bindingAdapterPosition]
                        if (product.additives.isEmpty() &&
                            product.requiredAdditiveGroups.isEmpty()
                        ) {
                            quantityCount = product.inBasketQuantity + 1

                            when (defineQuantityChangeStatus(quantityCount, product.inBasketQuantity)) {
                                QuantityChangeStatus.INCREASED -> increaseProduct(product, quantityCount)
                                QuantityChangeStatus.NO_CHANGES -> updateCounter(quantityCount)
                                else -> {
                                }
                            }
                        } else {
                            listener.onProductClick(product)
                        }
                    }

                    ivPlus.setOnClickListener {
                        val product = productCategoryModel[bindingAdapterPosition]
                        if (product.additives.isEmpty() &&
                            product.requiredAdditiveGroups.isEmpty()
                        ) {
                            quantityCount = product.inBasketQuantity + 1
                            updateCounter(quantityCount)

                            when (defineQuantityChangeStatus(quantityCount, product.inBasketQuantity)) {
                                QuantityChangeStatus.INCREASED -> increaseProduct(product, quantityCount)
                                QuantityChangeStatus.DECREASED -> listener.onMinusClick(
                                    product,
                                    product.inBasketQuantity - quantityCount
                                )
                                QuantityChangeStatus.NO_CHANGES -> updateCounter(quantityCount)
                            }
                        } else {
                            listener.onProductClick(product)
                        }
                    }

                    ivMinus.setOnClickListener {
                        val product = productCategoryModel[bindingAdapterPosition]
                        quantityCount = product.inBasketQuantity - 1
                        updateCounter(quantityCount)

                        when (defineQuantityChangeStatus(quantityCount, product.inBasketQuantity)) {
                            QuantityChangeStatus.INCREASED -> increaseProduct(product, quantityCount)
                            QuantityChangeStatus.DECREASED -> listener.onMinusClick(
                                product,
                                product.inBasketQuantity - quantityCount
                            )
                            QuantityChangeStatus.NO_CHANGES -> updateCounter(quantityCount)
                        }
                    }
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
            with(binding) {
                tvPortionCount.text = quantity.toString()
                if (quantity <= 0 || isOpen.not()) {
                    containerAddToBasket.makeVisible()
                    vgAddProduct.makeInvisible()
                    return
                }
                containerAddToBasket.makeGone()
                vgAddProduct.makeVisible()
            }
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
