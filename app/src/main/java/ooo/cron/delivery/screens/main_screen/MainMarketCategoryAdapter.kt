package ooo.cron.delivery.screens.main_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.databinding.ItemMainMarketCategoryBinding

class MainMarketCategoryAdapter(private val onClick: (category: MarketCategory) -> Unit) :
    ListAdapter<MarketCategory, MainMarketCategoryAdapter.MainMarketCategoryViewHolder>(
        MainMarketCategoryDiffUtil()
    ) {
    private var selectedCategoryPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainMarketCategoryViewHolder =
        MainMarketCategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_main_market_category, parent, false)
        )

    override fun onBindViewHolder(
        holder: MainMarketCategoryAdapter.MainMarketCategoryViewHolder,
        position: Int
    ) {
        getItem(position).let {
            holder.bind(it)
        }

    }

    override fun submitList(list: List<MarketCategory>?) {
        selectedCategoryPosition = 0
        super.submitList(list)
    }

    inner class MainMarketCategoryViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
            item.setOnClickListener {
                onClick(getItem(layoutPosition))
                selectedCategoryPosition = layoutPosition
                notifyDataSetChanged()
            }
        }

        private val binding = ItemMainMarketCategoryBinding.bind(item)

        fun bind(category: MarketCategory) {
            binding.tvMainMarketCategory.text = category.categoryName
            loadCategoryImg(category)

            if (selectedCategoryPosition == adapterPosition) {
                binding.ivMainMarketCategory.setStrokeWidthResource(R.dimen.main_market_categories_selected_stroke_width)
                binding.tvMainMarketCategory.setTextColor(itemView.resources.getColor(R.color.brand))
            } else {
                binding.ivMainMarketCategory.setStrokeWidthResource(R.dimen.main_market_categories_stroke_width)
                binding.tvMainMarketCategory.setTextColor(itemView.resources.getColor(R.color.grey80))
            }
        }

        private fun loadCategoryImg(category: MarketCategory) {
            Glide.with(binding.root)
                .load(category.categoryImgUri)
                .centerCrop()
                .into(binding.ivMainMarketCategory)
        }
    }
}


