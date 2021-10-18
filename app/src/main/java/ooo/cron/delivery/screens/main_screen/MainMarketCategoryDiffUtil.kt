package ooo.cron.delivery.screens.main_screen

import androidx.recyclerview.widget.DiffUtil
import ooo.cron.delivery.data.network.models.MarketCategory

class MainMarketCategoryDiffUtil: DiffUtil.ItemCallback<MarketCategory>() {
    override fun areItemsTheSame(oldItem: MarketCategory, newItem: MarketCategory): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MarketCategory, newItem: MarketCategory): Boolean =
        oldItem == newItem
}