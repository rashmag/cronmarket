package ooo.cron.delivery.screens.favorite_screen.view

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.FavoritePartners
import ooo.cron.delivery.data.network.models.Partner
import ooo.cron.delivery.databinding.ItemFavoritePartnersBinding

class FavoritePartnersAdapter(
    private val onPartnerClick: (partner: Partner) -> Unit
) : ListAdapter<Partner, FavoritePartnersAdapter.FavoritePartnersViewHolder>(DIFF_CALLBACK) {

    inner class FavoritePartnersViewHolder(private val itemBinding: ItemFavoritePartnersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(favoritePartner: Partner) {
            itemView.setOnClickListener{
                onPartnerClick.invoke(favoritePartner)
            }
            with(itemBinding) {
                loadImages(favoritePartner)
                tvMarketCategoryPartnerTitle.text = favoritePartner.name
                tvMarketCategoryPartnerShortDescription.text = favoritePartner.shortDescription
                tvMarketCategoryPartnerPrice.text =
                    itemBinding.root.context.getString(
                        R.string.market_category_partner_min_price,
                        favoritePartner.minAmountOrder.toInt()
                    )
                tvMarketCategoryPartnerRating.text = favoritePartner.rating.toString()
                tvMarketCategoryPartnerDelivery.visibility =
                    if (favoritePartner.minAmountDelivery != null) View.VISIBLE else View.GONE
                tvMarketCategoryPartnerDelivery.text = favoritePartner.minAmountDelivery.toString()
                if (favoritePartner.isOpen())
                    tvClosed.visibility = View.GONE
                else tvClosed.visibility = View.VISIBLE
                val openTime = favoritePartner.openTime()
                val openHours =
                    if (openTime[0] / 10 > 0) openTime[0] else "0${openTime[0]}"
                val openMinutes =
                    if (openTime[1] / 10 > 0) openTime[1] else "0${openTime[1]}"
                tvClosed.text = itemBinding.root.context.getString(
                    R.string.partner_closed,
                    "${openHours}: $openMinutes"
                )
            }
        }

        fun loadImages(favoritePartner: Partner) {
            with(itemBinding) {
                Glide.with(itemBinding.root)
                    .load(favoritePartner.mainWinImg)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_main)
                    .into(ivMarketCategoryPartner)
                Glide.with(itemBinding.root)
                    .load(favoritePartner.logo)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_main)
                    .into(itemBinding.ivMarketCategoryPartnerLogo)
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Partner>() {
            override fun areItemsTheSame(
                oldItem: Partner,
                newItem: Partner
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Partner,
                newItem: Partner
            ): Boolean =
                oldItem.id == newItem.id

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoritePartnersViewHolder(
        itemBinding = ItemFavoritePartnersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FavoritePartnersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}