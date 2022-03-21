package ooo.cron.delivery.screens.basket_screen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.databinding.ItemBasketPersonsBinding
import ooo.cron.delivery.screens.base.adapters.BaseViewHolder

class AdapterTableware(
    private val onQuantityEditClick: (quantity: Int) -> Unit
) : RecyclerView.Adapter<AdapterTableware.PersonsViewHolder>() {

    private var currentQuantity = 1

    fun updateQuantity(quantity: Int) {
        currentQuantity = quantity
        notifyItemChanged(0)
    }

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonsViewHolder {
        return PersonsViewHolder(
            ItemBasketPersonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PersonsViewHolder, position: Int) {
        holder.bind(currentQuantity)
    }

    inner class PersonsViewHolder(private val binding: ItemBasketPersonsBinding) : BaseViewHolder<Int>(binding.root) {

        override fun bind(item: Int) {
            with(binding) {

                // чекбокс
                with(swBasketPersons) {
                    isChecked = currentQuantity > 0
                    setOnCheckedChangeListener { _, isChecked ->
                        currentQuantity = if (isChecked) 1 else 0
                        onQuantityEditClick(currentQuantity)
                    }
                }

                // плашка -/+
                with(basketTablewareCounter) {

                    root.isVisible = currentQuantity > 0
                    ivBasketCounterPlus.isInvisible = currentQuantity >= AdapterBasket.DEVICE_MAX_COUNT

                    tvBasketCounterQuantity.text = currentQuantity.toString()

                    // Минус клик
                    ivBasketCounterMinus.setOnClickListener {
                        if (currentQuantity > 0) {
                            currentQuantity -= 1
                        } else {
                            currentQuantity = 1
                        }
                        onQuantityEditClick(currentQuantity)
                    }

                    // Плюс клик
                    ivBasketCounterPlus.setOnClickListener {
                        if (currentQuantity < AdapterBasket.DEVICE_MAX_COUNT) {
                            currentQuantity += 1
                        }
                        onQuantityEditClick(currentQuantity)
                    }
                }
            }
        }
    }
}