package ooo.cron.delivery.screens.partners_screen

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.App
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

        private lateinit var timer: CountDownTimer

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

                if (::timer.isInitialized) {
                    timer.cancel()
                }
                timer = object : CountDownTimer(1500, 1500) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d(this::class.simpleName, "Timer Restarted")
                    }

                    override fun onFinish() {
                        //listener.onPriceClick(product, position)
                    }

                }
                timer.start()
            }

            binding.ivPlus.setOnClickListener {
                var currentQuantity = binding.tvPortionCount.text.toString().toInt()
                binding.tvPortionCount.text = (++currentQuantity).toString()
                updateCounter(currentQuantity)

                if (::timer.isInitialized) {
                    timer.cancel()
                }
                timer = object : CountDownTimer(1500, 1500) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d(this::class.simpleName, "Timer Restarted")
                    }

                    override fun onFinish() {
                        //listener.onPlusClick(product, position)
                    }

                }
                timer.start()
            }

            binding.ivMinus.setOnClickListener {
                var currentQuantity = binding.tvPortionCount.text.toString().toInt()
                binding.tvPortionCount.text = (--currentQuantity).toString()
                updateCounter(currentQuantity)

                if (::timer.isInitialized) {
                    timer.cancel()
                }
                timer = object : CountDownTimer(1500, 1500) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d(this::class.simpleName, "Timer Restarted")
                    }

                    override fun onFinish() {
//                        listener.onMinusClick(product, position)
                    }

                }
                timer.start()
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
    SectionRecyclerViewAdapter<PartnerProductAdapter.ViewHolder, ProductCategoryModel>(productCategoryModel,
        GridLayoutManager(App.context, 2)) {

    override fun viewHolder(view: View) = ViewHolder(view)


    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            recyclerView.adapter =
                CategoryAdapter(productCategoryModel[position].productList, listener)
        }
    }
}
