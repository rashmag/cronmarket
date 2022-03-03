package ooo.cron.delivery.screens.base.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseConcatBlockAdapter<T>(
    private val item: T? = null
) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    override fun getItemCount() = 1
    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        if (item != null) {
            holder.bind(item = item)
        }
    }
}