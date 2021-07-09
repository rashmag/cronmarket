package ooo.cron.delivery.utils.section_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.fajaragungpramana.sectionrecyclerview.Section
import ooo.cron.delivery.R

abstract class SectionRecyclerViewAdapter<VH : SectionRecyclerViewHolder, M : Section> :
    RecyclerView.Adapter<VH>() {

    abstract fun getSectionList(): List<M>

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

}