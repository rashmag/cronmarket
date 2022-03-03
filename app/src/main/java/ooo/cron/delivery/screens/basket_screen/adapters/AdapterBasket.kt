package ooo.cron.delivery.screens.basket_screen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.databinding.ItemBasketProductBinding
import ooo.cron.delivery.screens.base.adapters.BaseViewHolder
import ooo.cron.delivery.screens.basket_screen.BasketAdditiveAdapter
import ooo.cron.delivery.utils.dipToPixels
import ooo.cron.delivery.utils.extensions.uiLazy

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class AdapterBasket(
    private val plusClick: (dish: BasketDish, extraQuantity: Int) -> Unit,
    private val minusClick: (dish: BasketDish, unwantedQuantity: Int) -> Unit
) : ListAdapter<BasketDish, AdapterBasket.ProductViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemBasketProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemBasketProductBinding) :
        BaseViewHolder<BasketDish>(binding.root) {

        var product: BasketDish? = null
        private val productAdapter by uiLazy {
            BasketAdditiveAdapter()
        }

        override fun bind(item: BasketDish) {
            this.product = item

            Glide.with(itemView.context)
                .load(item.photoUri)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(itemView.context.resources.dipToPixels(3f).toInt())
                    )
                )
                .into(binding.ivBasketProduct)

            with(binding) {
                tvBasketProductName.text = item.name
                with(binding.rvAdditiveBasket) {
                    if (item.dishAdditives.isNotEmpty()) {
                        visibility = View.VISIBLE
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = productAdapter.apply { updateDishAdditiveList(item.dishAdditives) }
                    } else {
                        visibility = View.GONE
                    }
                }

                basketProductCounter.tvBasketCounterQuantity.text = item.quantity.toString()
                tvBasketProductAmount.text = itemView.context.getString(
                    R.string.price,
                    (item.cost * item.quantity).toString()
                )

                basketProductCounter.ivBasketCounterPlus.setOnClickListener {
                    plusClick(item, 1)
                }

                basketProductCounter.ivBasketCounterMinus.setOnClickListener {
                    minusClick(item, 1)
                }
            }
        }
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<BasketDish>() {
            override fun areItemsTheSame(oldItem: BasketDish, newItem: BasketDish): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BasketDish, newItem: BasketDish): Boolean =
                oldItem.id == newItem.id
                        && oldItem.productId == newItem.productId
                        && oldItem.name == newItem.name
                        && oldItem.cost == newItem.cost
                        && oldItem.quantity == newItem.quantity
                        && oldItem.dishAdditives == newItem.dishAdditives
        }

        const val DEVICE_MAX_COUNT = 20
    }
}