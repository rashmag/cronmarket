package ooo.cron.delivery.screens.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.databinding.ItemHeaderAdapterBinding

class AdapterHeader(
    private val title: String
) : RecyclerView.Adapter<AdapterHeader.HeaderViewHolder>() {

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HeaderViewHolder(
        itemBinding = ItemHeaderAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(title)
    }

    inner class HeaderViewHolder(
        private val itemBinding: ItemHeaderAdapterBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(title: String) {
            itemBinding.root.text = title
        }
    }
}