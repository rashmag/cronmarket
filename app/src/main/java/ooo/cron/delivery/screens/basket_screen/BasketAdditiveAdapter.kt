package ooo.cron.delivery.screens.basket_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketDishAdditive
import ooo.cron.delivery.databinding.CardViewBasketAdditiveBinding
import ooo.cron.delivery.screens.base.adapters.BaseViewHolder

class BasketAdditiveAdapter : RecyclerView.Adapter<BasketAdditiveAdapter.AdditiveItemViewHolder>() {

    private var basketDishAdditiveList: ArrayList<BasketDishAdditive> = arrayListOf()

    fun updateDishAdditiveList(list: List<BasketDishAdditive>) {
        basketDishAdditiveList.run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditiveItemViewHolder {
        val binding = CardViewBasketAdditiveBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdditiveItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdditiveItemViewHolder, position: Int) {
        val foodItem = basketDishAdditiveList[position]
        holder.bind(foodItem)
    }

    override fun getItemCount(): Int = basketDishAdditiveList.size

    class AdditiveItemViewHolder(private val binding: CardViewBasketAdditiveBinding) :
        BaseViewHolder<BasketDishAdditive>(binding.root) {

        override fun bind(item: BasketDishAdditive) {
            with(binding) {
                if (item.cost == 0.0) {
                    tvAdditiveBasketName.text = item.name
                    return
                }
                tvAdditiveBasketName.text = root.context.getString(
                    R.string.partner_product_additive_name,
                    item.name
                )
                tvAdditiveBasketCost.text = root.context.getString(
                    R.string.partner_product_additive_price,
                    item.cost
                )
            }
        }
    }
}
