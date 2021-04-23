package ooo.cron.delivery.screens.market_category_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Partner
import ooo.cron.delivery.databinding.ItemMarketCategoryPartnerBinding

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class PartnersAdapter :
    PagedListAdapter<Partner, PartnersAdapter.PartnerViewHolder>(Partner.DIFF_CALBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartnerViewHolder = PartnerViewHolder.create(parent)

    override fun onBindViewHolder(holder: PartnerViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class PartnerViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding = ItemMarketCategoryPartnerBinding.bind(itemView)

        fun bind(partner: Partner) {
            binding.tvMarketCategoryPartnerTitle.text = partner.name
        }

        companion object {
            fun create(parent: ViewGroup) =
                PartnerViewHolder(
                    LayoutInflater.from(parent.context!!)
                        .inflate(
                            R.layout.item_market_category_partner,
                            parent,
                            false
                        )
                )
        }
    }
}
