package ooo.cron.delivery.screens.basket_screen

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketDishAdditive
import ooo.cron.delivery.databinding.CardViewBasketAdditiveBinding
class BasketAdditiveAdapter(basketDishAdditiveList: List<BasketDishAdditive>) :
    RecyclerView.Adapter<BasketAdditiveAdapter.AdditiveItemViewHolder>() {
    private var basketDishAdditiveList: List<BasketDishAdditive> = listOf()

    init {
        this.basketDishAdditiveList = basketDishAdditiveList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BasketAdditiveAdapter.AdditiveItemViewHolder {
        val binding = CardViewBasketAdditiveBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdditiveItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketAdditiveAdapter.AdditiveItemViewHolder, position: Int) {
        val foodItem = basketDishAdditiveList[position]
        if (holder is AdditiveItemViewHolder)
            holder.bind(foodItem)
    }

    override fun getItemCount(): Int {
        return basketDishAdditiveList.size
    }

    class AdditiveItemViewHolder(private val binding: CardViewBasketAdditiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(basketDishAdditive: BasketDishAdditive) {
            if (basketDishAdditive.cost != 0.0) {
                binding.tvAdditiveBasketName.text = "+ " + basketDishAdditive.name
                binding.tvAdditiveBasketCost.text = "+" + basketDishAdditive.cost + "₽"
            } else {
                binding.tvAdditiveBasketName.text = basketDishAdditive.name
            }
        }
    }
}

