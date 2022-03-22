package ooo.cron.delivery.utils.additive_section_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.fajaragungpramana.sectionrecyclerview.Section
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.RequireAdditiveModel

abstract class AdapterAdditiveSectionRecyclerView<VH : HolderAdditiveSectionRecyclerView, M : Section>
    : ListAdapter<RequireAdditiveModel, VH>(DIFF_CALLBACK) {

    abstract fun getSectionList(): ArrayList<RequireAdditiveModel>

    protected abstract fun viewHolder(view: View): VH

    override fun getItemCount() = getSectionList().size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        viewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_adapter_section_additive_recyclerview,
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RequireAdditiveModel>() {
            override fun areItemsTheSame(oldItem: RequireAdditiveModel, newItem: RequireAdditiveModel): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: RequireAdditiveModel, newItem: RequireAdditiveModel): Boolean =
                oldItem.additiveName == newItem.additiveName && oldItem.additiveList == newItem.additiveList
        }
    }
}