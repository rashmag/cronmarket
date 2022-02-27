package ooo.cron.delivery.screens.basket_screen

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketItem
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.databinding.ItemBasketHeaderBinding
import ooo.cron.delivery.databinding.ItemBasketPersonsBinding
import ooo.cron.delivery.databinding.ItemBasketProductBinding
import ooo.cron.delivery.databinding.ItemBasketSpecialOffersBinding
import ooo.cron.delivery.utils.dipToPixels
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeInvisible
import ooo.cron.delivery.utils.extensions.makeVisible

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class BasketAdapter(
    private val isRestaurant: Int
) : ListAdapter<BasketDish,RecyclerView.ViewHolder>(BasketDiffUtil()) {

    private var products: List<BasketDish> = listOf()

    private var plusClick: (dish: BasketDish, extraQuantity: Int) -> Unit = { _, _ -> }
    private var minusClick: (dish: BasketDish, unwantedQuantity: Int) -> Unit = { _, _ -> }

    private var partnersEditClick: (quantity: Int) -> Unit = { _ -> }
    private var persons = 0

    override fun getItemViewType(position: Int): Int =
        when (position) {
            getHeaderPosition() -> R.layout.item_basket_header
            getPersonsPosition() -> {
                if (isRestaurant == RESTAURANT) {
                    R.layout.item_basket_persons
                } else {
                    R.layout.item_basket_special_offers
                }
            }
            getSpecialOffersPosition() -> R.layout.item_basket_special_offers
            else -> R.layout.item_basket_product
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_basket_header -> HeaderViewHolder(inflateHeader(parent))
            R.layout.item_basket_persons -> PersonsViewHolder(inflatePersons(parent))
            R.layout.item_basket_special_offers -> SpecialOffersBinding(inflateSpecialOffers(parent))
            else -> ProductViewHolder(inflateProduct(parent))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder)
            holder.bind(getProduct(position))
        if (holder is PersonsViewHolder)
            holder.bind(persons)
    }

    override fun getItemCount(): Int =
        products.size + 3

    private fun inflateProduct(parent: ViewGroup) =
        ItemBasketProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

    private fun inflateHeader(parent: ViewGroup) =
        ItemBasketHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

    private fun inflatePersons(parent: ViewGroup) =
        ItemBasketPersonsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

    private fun inflateSpecialOffers(parent: ViewGroup) =
        ItemBasketSpecialOffersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

    private fun getHeaderPosition() = 0
    private fun getPersonsPosition() = getProductPositionRange().last + 1
    private fun getSpecialOffersPosition() = getPersonsPosition() + 1

    private fun getProductPositionRange(): IntRange {
        val a = 1..products.size
        return a
    }

    private fun getProduct(position: Int) = products[position - 1]

    fun setProducts(
        products: List<BasketDish>,
        persons: Int,
        onPlusClick: (dish: BasketDish, extraQuantity: Int) -> Unit,
        onMinusClick: (dish: BasketDish, unwantedQuantity: Int) -> Unit,
        partnersEditClick: (quantity: Int) -> Unit
    ) {
        this.products = products
        this.plusClick = onPlusClick
        this.minusClick = onMinusClick
        this.partnersEditClick = partnersEditClick
        this.persons = persons
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(private val binding: ItemBasketProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var product: BasketDish? = null

        fun bind(product: BasketDish) {
            this.product = product

            binding.tvBasketProductName.text = product.name
            with(binding.rvAdditiveBasket) {
                if(product.dishAdditives.isNotEmpty()) {
                    visibility = View.VISIBLE
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = BasketAdditiveAdapter(product.dishAdditives)
                } else {
                    visibility = View.GONE
                }
            }
            binding.vgBasketCounter.tvBasketCounterQuantity.text = product.quantity.toString()
            binding.tvBasketProductAmount.text = itemView.context.getString(
                R.string.price,
                (product.cost * product.quantity).toString()
            )

            binding.vgBasketCounter.ivBasketCounterPlus.setOnClickListener {
                plusClick(product, 1)
            }

            binding.vgBasketCounter.ivBasketCounterMinus.setOnClickListener {
                minusClick(product, 1)
            }

            Glide.with(itemView.context)
                .load(product.photoUri)
                .apply(RequestOptions().apply {
                    transform(
                        CenterCrop(),
                        RoundedCorners(
                            itemView.context.resources.dipToPixels(3f).toInt()
                        )
                    )
                }).into(binding.ivBasketProduct)
        }
    }

    private class HeaderViewHolder(
        headerBinding: ItemBasketHeaderBinding
    ) : RecyclerView.ViewHolder(headerBinding.root)

    private inner class PersonsViewHolder(
        private val binding: ItemBasketPersonsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var currentQuantity = 0

        fun bind(quantity: Int) {
            currentQuantity = quantity

            with(binding) {

                // Чекбокс
                swBasketPersons.apply {

                    isChecked = currentQuantity >= 1
                    vgBasketCounter.ivBasketCounterMinus.isVisible = currentQuantity >= 0

                    if (currentQuantity == DEVICE_MAX_COUNT) vgBasketCounter.ivBasketCounterPlus.makeGone()

                    setOnCheckedChangeListener { _, isChecked ->
                        vgBasketCounter.root.isVisible = isChecked
                    }
                }

                vgBasketCounter.tvBasketCounterQuantity.text =
                    if (currentQuantity == 0) (currentQuantity + 1).toString()
                    else currentQuantity.toString()

                vgBasketCounter.root.isVisible = currentQuantity > 0

                // Минус клик
                vgBasketCounter.ivBasketCounterMinus.setOnClickListener {
                    if (currentQuantity > 0) currentQuantity--

                    vgBasketCounter.ivBasketCounterPlus.makeVisible()

                    partnersEditClick(currentQuantity)
                }

                // Плюс клик
                vgBasketCounter.ivBasketCounterPlus.setOnClickListener {
                    if (currentQuantity == 0) {
                        currentQuantity += 1
                    }

                    vgBasketCounter.ivBasketCounterMinus.makeVisible()

                    if (currentQuantity <= DEVICE_MAX_COUNT) currentQuantity++

                    if (currentQuantity == DEVICE_MAX_COUNT)
                        it.makeInvisible()
                    else
                        it.makeVisible()

                    partnersEditClick(currentQuantity)
                }
            }
        }
    }

    private class SpecialOffersBinding(
        specialOffersBinding: ItemBasketSpecialOffersBinding
    ) : RecyclerView.ViewHolder(specialOffersBinding.root)

    companion object {
        const val RESTAURANT = 1
        const val DEVICE_MAX_COUNT = 20
    }

    class BasketDiffUtil: DiffUtil.ItemCallback<BasketDish>() {
        override fun areItemsTheSame(oldItem: BasketDish, newItem: BasketDish): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BasketDish, newItem: BasketDish): Boolean =
            oldItem == newItem
    }
}