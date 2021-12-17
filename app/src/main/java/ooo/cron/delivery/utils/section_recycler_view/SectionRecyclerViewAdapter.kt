package ooo.cron.delivery.utils.section_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.fajaragungpramana.sectionrecyclerview.Section
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.ProductCategoryModel

abstract class SectionRecyclerViewAdapter<VH : SectionRecyclerViewHolder, M : Section>
    : ListAdapter<ProductCategoryModel, VH>(DIFF_CALLBACK) {

    abstract fun getSectionList(): ArrayList<ProductCategoryModel>

    protected abstract fun viewHolder(view: View): VH

    override fun getItemCount() = getSectionList().size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        viewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_adapter_section_recycerview,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindSectionTitle(getSectionList()[position].titleSection, position)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductCategoryModel>() {
            override fun areItemsTheSame(oldItem: ProductCategoryModel, newItem: ProductCategoryModel): Boolean =
                oldItem.categoryId == newItem.categoryId

            override fun areContentsTheSame(oldItem: ProductCategoryModel, newItem: ProductCategoryModel): Boolean =
                oldItem.categoryName == newItem.categoryName && oldItem.productList == newItem.productList
        }
    }
}