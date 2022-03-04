package ooo.cron.delivery.screens.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Dimension.DP
import ooo.cron.delivery.databinding.ItemSpacingAdapterBinding
import ooo.cron.delivery.utils.extensions.requestNewSize

class AdapterSpace(
    private val space: SpacingValue
) : BaseConcatBlockAdapter<SpacingValue>(item = space) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemBinding = ItemSpacingAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(itemBinding: ItemSpacingAdapterBinding) : BaseViewHolder<SpacingValue>(itemBinding.root) {

        init {
            itemBinding.root.requestNewSize(height = space.value * DP)
        }
    }
}