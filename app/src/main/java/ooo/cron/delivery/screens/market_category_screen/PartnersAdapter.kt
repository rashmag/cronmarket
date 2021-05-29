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
class PartnersAdapter(private val onClick: (partnerId: String) -> Unit) :
    PagedListAdapter<Partner, RecyclerView.ViewHolder>(Partner.DIFF_CALLBACK) {

    override fun getItem(position: Int): Partner? {
        return differ.getItem(position)
    }

    override fun submitList(pagedList: PagedList<Partner>?) {
        differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<Partner>? {
        return differ.currentList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        PartnerViewHolder(
            LayoutInflater.from(parent.context!!).inflate(
                R.layout.item_market_category_partner, parent, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PartnerViewHolder) {
            getItem(position)?.let {
                holder.bind(it, onClick)
            }
        }
    }

    override fun getItemCount(): Int =
        differ.itemCount

    val adapterCallback = AdapterListUpdateCallback(this)

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapterCallback.onInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapterCallback.onRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapterCallback.onMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapterCallback.onChanged(position, count, payload)
        }
    }

    private val differ = AsyncPagedListDiffer(
        listUpdateCallback,
        AsyncDifferConfig.Builder(Partner.DIFF_CALLBACK).build()
    )

    class PartnerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ItemMarketCategoryPartnerBinding.bind(itemView)

        fun bind(partner: Partner, onClick: (partnerId: String) -> Unit) {

            binding.root.setOnClickListener{ onClick(partner.id) }

            binding.tvMarketCategoryPartnerTitle.text = partner.name
            binding.tvMarketCategoryPartnerShortDescription.text = partner.shortDescription
            binding.tvMarketCategoryPartnerRating.text = partner.rating.toString()
            binding.tvMarketCategoryPartnerMinPrice.text =
                binding.root.context.getString(
                    R.string.market_category_partner_min_price,
                    partner.minAmountOrder.toInt()
                )

            loadImage(partner)
            loadLogo(partner)

        }

        private fun loadImage(partner: Partner) {
            if (partner.mainWinImg == null || partner.mainWinImg.isEmpty())
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
