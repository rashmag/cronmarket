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
                    tvAdditiveBasketName.text = root.context.getString(
                        R.string.partner_product_additive_name,
                        refactorName(item.name)
                    )
                    return
                }

                tvAdditiveBasketName.text = root.context.getString(
                    R.string.partner_product_additive_name,
                    refactorName(item.name)
                )
                tvAdditiveBasketCost.text = root.context.getString(
                    R.string.partner_product_additive_price,
                    item.cost.toInt()
                )
            }
        }

        fun refactorName(name: String):String{
            if(name.isNotEmpty()){
                if(name.substring(0,1).equals(" ")){
                    val nameRefactor = name.substring(1)
                    if(nameRefactor[0].isLowerCase())
                        return nameRefactor.substring(0,1).toUpperCase() + nameRefactor.substring(1)
                    else
                        return nameRefactor
                }else{
                    if(name[0].isLowerCase())
                        return name.substring(0,1).toUpperCase() + name.substring(1)
                    else
                        return name
                }
            }
            return ""
        }
    }
}
