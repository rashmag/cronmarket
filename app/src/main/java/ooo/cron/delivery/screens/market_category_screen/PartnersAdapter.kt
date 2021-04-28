package ooo.cron.delivery.screens.market_category_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Partner
import ooo.cron.delivery.databinding.ItemMarketCategoryPartnerBinding

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class PartnersAdapter(
    val onCreateTags: (tagsList: RecyclerView) -> Unit
) :
    PagedListAdapter<Partner, RecyclerView.ViewHolder>(Partner.DIFF_CALLBACK) {

    override fun getItem(position: Int): Partner? {
        return differ.getItem(position - 1)
    }

    override fun submitList(pagedList: PagedList<Partner>?) {
        differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<Partner>? {
        return differ.currentList
    }

    override fun getItemViewType(position: Int): Int =
        if (position > 0) R.layout.item_market_category_partner
        else R.layout.item_market_category_header

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        if (viewType == R.layout.item_market_category_header)
            HeaderViewHolder(
                LayoutInflater.from(parent.context!!).inflate(
                    R.layout.item_market_category_header, parent, false
                ).also {
                    onCreateTags(it.findViewById(R.id.rv_market_category_tags))
                }
            )
        else PartnerViewHolder(
            LayoutInflater.from(parent.context!!).inflate(
                R.layout.item_market_category_partner, parent, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PartnerViewHolder)
            getItem(position)?.let {
                holder.bind(it)
            }
    }

    override fun getItemCount(): Int =
        differ.itemCount + 1

    val adapterCallback = AdapterListUpdateCallback(this)

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapterCallback.onInserted(position + 1, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapterCallback.onRemoved(position + 1, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapterCallback.onChanged(position + 1, count, payload)
        }
    }

    private val differ = AsyncPagedListDiffer(
        listUpdateCallback,
        AsyncDifferConfig.Builder(Partner.DIFF_CALLBACK).build()
    )

    class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    class PartnerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ItemMarketCategoryPartnerBinding.bind(itemView)

        fun bind(partner: Partner) {
            binding.tvMarketCategoryPartnerTitle.text = partner.name
            binding.tvMarketCategoryPartnerShortDescription.text = partner.shortDescription
            binding.tvMarketCategoryPartnerRating.text = partner.rating.toString()
            binding.tvMarketCategoryPartnerMinPrice.text = partner.minAmountOrder.toInt().toString()

            loadImage(partner)
            loadLogo(partner)

        }

        private fun loadImage(partner: Partner) {
            if (partner.mainWinImg.isEmpty())
                return

            Glide.with(binding.root)
                .load(partner.mainWinImg)
                .centerCrop()
                .into(binding.ivMarketCategoryPartner)
        }

        private fun loadLogo(partner: Partner) {
            if (partner.logo.isEmpty())
                return

            Glide.with(binding.root)
                .load(partner.logo)
                .centerCrop()
                .into(binding.ivMarketCategoryPartnerLogo)

            binding.ivMarketCategoryPartnerLogo.visibility = View.VISIBLE
        }
    }
}
