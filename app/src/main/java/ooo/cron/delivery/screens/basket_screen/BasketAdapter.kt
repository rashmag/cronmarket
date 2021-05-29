package ooo.cron.delivery.screens.basket_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketItem
import ooo.cron.delivery.databinding.ItemBasketHeaderBinding
import ooo.cron.delivery.databinding.ItemBasketPersonsBinding
import ooo.cron.delivery.databinding.ItemBasketProductBinding
import ooo.cron.delivery.databinding.ItemBasketSpecialOffersBinding
import ooo.cron.delivery.utils.BasketCounterTimer
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class BasketAdapter @Inject constructor(

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var products: List<BasketItem> = listOf()

    override fun getItemViewType(position: Int): Int =
        when (position) {
            getHeaderPosition() -> R.layout.item_basket_header
            getPersonsPosition() -> R.layout.item_basket_persons
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
        val a = 1 .. products.size
        return a
    }
    private fun getProduct(position: Int) = products[position - 1]

    fun setProducts(products: List<BasketItem>) {
        this.products = products
    }

    inner class ProductViewHolder(private val binding: ItemBasketProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val timer = BasketCounterTimer(1500, 1500)

        fun bind(product: BasketItem) {
            binding.tvBasketProductName.text = product.name
            binding.vgBasketCounter.tvBasketCounterQuantity.text = product.quantity.toString()
            binding.tvBasketProductAmount.text = product.getAmount().toString()

            binding.vgBasketCounter.ivBasketCounterMinus.setOnClickListener {
                TODO()
            }

            binding.vgBasketCounter.ivBasketCounterPlus.setOnClickListener {
                TODO()
            }

            Glide.with(itemView.context)
                .load(product.photoUri)
                .into(binding.ivBasketProduct)
        }
    }

    private class HeaderViewHolder(
        headerBinding: ItemBasketHeaderBinding
    ) : RecyclerView.ViewHolder(headerBinding.root)

    private class PersonsViewHolder(
        personsBinding: ItemBasketPersonsBinding
    ) : RecyclerView.ViewHolder(personsBinding.root)

    private class SpecialOffersBinding(
        specialOffersBinding: ItemBasketSpecialOffersBinding
    ) : RecyclerView.ViewHolder(specialOffersBinding.root)
}