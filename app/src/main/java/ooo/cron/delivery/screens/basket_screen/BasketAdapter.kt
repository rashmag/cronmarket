package ooo.cron.delivery.screens.basket_screen

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketItem
import ooo.cron.delivery.databinding.ItemBasketHeaderBinding
import ooo.cron.delivery.databinding.ItemBasketPersonsBinding
import ooo.cron.delivery.databinding.ItemBasketProductBinding
import ooo.cron.delivery.databinding.ItemBasketSpecialOffersBinding
import ooo.cron.delivery.utils.BasketCounterTimer
import ooo.cron.delivery.utils.dipToPixels
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class BasketAdapter(
    private val isRestaurant: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var products: List<BasketDish> = listOf()

    private var plusClick: (dish: BasketDish, extraQuantity: Int) -> Unit = { _, _ -> }
    private var minusClick: (dish: BasketDish, unwantedQuantity: Int) -> Unit = { _, _ -> }

    private var partnersEditClick: (quantity: Int) -> Unit = { _ -> }
    private var persons = 0

    override fun getItemViewType(position: Int): Int =
        when (position) {
            getHeaderPosition() -> R.layout.item_basket_header
            getPersonsPosition() -> {
                if (isRestaurant == RESTAURANT){
                    R.layout.item_basket_persons
                }else{
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
    }

    inner class ProductViewHolder(private val binding: ItemBasketProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var product: BasketDish

        private val timer = BasketCounterTimer()

        fun bind(product: BasketDish) {
            this.product = product

            binding.tvBasketProductName.text = product.name
            binding.vgBasketCounter.tvBasketCounterQuantity.text = product.quantity.toString()
            binding.tvBasketProductAmount.text = itemView.context.getString(
                R.string.price,
                (product.cost * product.quantity).toInt().toString()
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
                })
                .into(binding.ivBasketProduct)
        }
    }

    private class HeaderViewHolder(
        headerBinding: ItemBasketHeaderBinding
    ) : RecyclerView.ViewHolder(headerBinding.root)

    private inner class PersonsViewHolder(
        private val binding: ItemBasketPersonsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var timer: CountDownTimer? = null
        var currentQuantity = 0

        fun bind(quantity: Int) {
            currentQuantity = quantity
            binding.swBasketPersons.apply {
                if (quantity > 0)
                    isChecked = quantity > 0
                else
                    binding.vgBasketCounter.ivBasketCounterMinus.visibility = View.INVISIBLE

                if (quantity == 20)
                    binding.vgBasketCounter.ivBasketCounterPlus.visibility = View.INVISIBLE
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                        binding.vgBasketCounter.root.visibility = View.VISIBLE
                    else
                        binding.vgBasketCounter.root.visibility = View.INVISIBLE
                }
            }

            binding.vgBasketCounter.tvBasketCounterQuantity.text = currentQuantity.toString()

            if (quantity > 0)
                binding.vgBasketCounter.root.visibility = View.VISIBLE
            else
                binding.vgBasketCounter.root.visibility = View.INVISIBLE

            binding.vgBasketCounter.ivBasketCounterMinus.setOnClickListener {
                if (currentQuantity > 0) currentQuantity--
                if (currentQuantity == 0)
                    it.visibility = View.INVISIBLE
                else
                    it.visibility = View.VISIBLE

                if (!binding.vgBasketCounter.ivBasketCounterPlus.isVisible) {
                    binding.vgBasketCounter.ivBasketCounterPlus.visibility = View.VISIBLE
                }
                editQuantity(currentQuantity)
            }

            binding.vgBasketCounter.ivBasketCounterPlus.setOnClickListener {
                if (currentQuantity <= 20) currentQuantity++

                if (currentQuantity == 20)
                    it.visibility = View.INVISIBLE
                else
                    it.visibility = View.VISIBLE

                if (!binding.vgBasketCounter.ivBasketCounterMinus.isVisible) {
                    binding.vgBasketCounter.ivBasketCounterMinus.visibility = View.VISIBLE
                }

                editQuantity(currentQuantity)
            }
        }

        private fun editQuantity(quantity: Int) {
            binding.vgBasketCounter.tvBasketCounterQuantity.text = quantity.toString()
            timer?.cancel()
            timer = object : CountDownTimer(900, 900) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.d(
                        this::class.simpleName,
                        "persons quantity changed"
                    )
                }

                override fun onFinish() {
                    binding.vgBasketCounter.root.visibility = View.INVISIBLE
                    binding.pbPersonsProgress.visibility = View.VISIBLE
                    partnersEditClick(currentQuantity)
                }
            }
            timer!!.start()
        }
    }

    private class SpecialOffersBinding(
        specialOffersBinding: ItemBasketSpecialOffersBinding
    ) : RecyclerView.ViewHolder(specialOffersBinding.root)

    companion object{
        const val RESTAURANT = 1
    }
}