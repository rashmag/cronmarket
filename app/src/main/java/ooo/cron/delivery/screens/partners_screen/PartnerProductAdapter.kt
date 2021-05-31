package ooo.cron.delivery.screens.partners_screen

import android.os.CountDownTimer
import android.util.Log
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
import java.util.*

/*
 * Created by Muhammad on 08.05.2021
 */



class CategoryAdapter(
    private val productCategoryModel: List<PartnerProductsRes>,
    private val listener: OnProductClickListener,
    private val recyclerViewPosition: Int
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

        private val timer = BasketCounterTimer(1500, 1500)

        fun bindProduct(position: Int) {
            val product = productCategoryModel[position]
            binding.run {
                with(product) {
                    tvProductName.text = name
                    tvCost.text = cost.toString()
                    tvGram.text = portionSize

                    updateCounter(inBasketQuantity)

                    com.bumptech.glide.Glide.with(root)
                        .load(photo)
                        .into(ivProduct)
                }
            }

            binding.tvCost.setOnClickListener {
                var currentQuantity = binding.tvPortionCount.text.toString().toInt()
                binding.tvPortionCount.text = (++currentQuantity).toString()
                updateCounter(currentQuantity)

                val quantityChangeStatus =
                    defineQuantityChangeStatus(currentQuantity, product.inBasketQuantity)

                if (quantityChangeStatus ==
                    QuantityChangeStatus.INCREASED
                ) {
                    if (product.additives.isEmpty() &&
                        product.requiredAdditiveGroups.isEmpty()
                    )
                        return@setOnClickListener restartTimer {
                            listener.onPlusClick(
                                product,
                                listOf(),
                                currentQuantity - product.inBasketQuantity,
                                recyclerViewPosition
                            )
                        }
                    TODO("Should show product dialog")
                }
            }

            binding.ivPlus.setOnClickListener {
                var currentQuantity = binding.tvPortionCount.text.toString().toInt()
                binding.tvPortionCount.text = (++currentQuantity).toString()
                updateCounter(currentQuantity)

                val quantityChangeStatus =
                    defineQuantityChangeStatus(currentQuantity, product.inBasketQuantity)

                when (quantityChangeStatus) {
                    QuantityChangeStatus.INCREASED -> {
                        if (product.additives.isEmpty() &&
                            product.requiredAdditiveGroups.isEmpty()
                        )
                            return@setOnClickListener restartTimer {
                                listener.onPlusClick(
                                    product,
                                    listOf(),
                                    currentQuantity - product.inBasketQuantity,
                                    position
                                )
                            }
                        TODO("Should show product dialog")
                    }

                    QuantityChangeStatus.DECREASED ->
                        restartTimer {
                            listener.onMinusClick(product, position)
                        }
                }
            }

            binding.ivMinus.setOnClickListener {
                var currentQuantity = binding.tvPortionCount.text.toString().toInt()
                binding.tvPortionCount.text = (--currentQuantity).toString()
                updateCounter(currentQuantity)

                val quantityChangeStatus =
                    defineQuantityChangeStatus(currentQuantity, product.inBasketQuantity)

                when (quantityChangeStatus) {
                    QuantityChangeStatus.INCREASED -> {
                        if (product.additives.isEmpty() &&
                            product.requiredAdditiveGroups.isEmpty()
                        )
                            return@setOnClickListener restartTimer {
                                listener.onPlusClick(
                                    product,
                                    listOf(),
                                    currentQuantity - product.inBasketQuantity,
                                    position
                                )
                            }
                        TODO("Should show product dialog")
                    }

                    QuantityChangeStatus.DECREASED ->
                        restartTimer {
                            listener.onMinusClick(product, position)
                        }
                }
            }
        }

        private fun updateCounter(quantity: Int) {
            if (quantity <= 0) {
                binding.tvCost.visibility = View.VISIBLE
                binding.vgAddProduct.visibility = View.INVISIBLE
                return
            }
            binding.tvPortionCount.text = quantity.toString()
            binding.tvCost.visibility = View.INVISIBLE
            binding.vgAddProduct.visibility = View.VISIBLE
        }

        private fun restartTimer(
            onFinish: () -> Unit
        ) {
            timer.cancel()
            timer.start {
                binding.partnerProductProgress.visibility = View.VISIBLE
                binding.vgCost.visibility = View.INVISIBLE
                onFinish()
            }
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

    private enum class QuantityChangeStatus {
        INCREASED, DECREASED, NO_CHANGES
    }

    interface OnProductClickListener {
        fun onProductClick(product: PartnerProductsRes)
        fun onPlusClick(
            product: PartnerProductsRes,
            additives: List<BasketDishAdditive>,
            quantity: Int,
            position: Int
        )

        fun onMinusClick(product: PartnerProductsRes, position: Int)
    }

}

class PartnerProductAdapter(
    var productCategoryModel: List<ProductCategoryModel>,
    private val listener: CategoryAdapter.OnProductClickListener
) :
    SectionRecyclerViewAdapter<PartnerProductAdapter.ViewHolder, ProductCategoryModel>(
        productCategoryModel
    ) {

    override fun viewHolder(view: View) = ViewHolder(view)


    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            recyclerView.adapter =
                CategoryAdapter(productCategoryModel[position].productList, listener, position)
        }
    }
}
