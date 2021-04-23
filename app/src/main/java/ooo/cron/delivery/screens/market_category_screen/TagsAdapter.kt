package ooo.cron.delivery.screens.market_category_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Tag
import ooo.cron.delivery.data.network.models.TagsResult
import ooo.cron.delivery.utils.dipToPixels

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class TagsAdapter(
    val onTagClick: (tag: Tag) -> Unit,
    val onAllTagsClick: () -> Unit,
    val onOthersTagsClick: () -> Unit
) :
    RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private var tagsToShow: TagsResult? = null
    private var selectedTagPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context!!)
                .inflate(R.layout.item_market_category_tag, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tagsToShow?.let { tags ->
            (holder.itemView as TextView).apply {

                when (position) {
                    0 -> {
                        text = context!!.getString(R.string.market_category_all_tags)
                        setOnClickListener {
                            selectedTagPosition = position
                            notifyDataSetChanged()
                            onAllTagsClick()
                        }
                    }

                    itemCount - 1 -> {
                        text = "..."
                        setOnClickListener {
                            selectedTagPosition = position
                            notifyDataSetChanged()
                            onOthersTagsClick()
                        }
                    }

                    else -> {
                        text = tags.tags[position].name
                        setOnClickListener {
                            selectedTagPosition = position
                            notifyDataSetChanged()
                            onTagClick(tags.tags[position])
                        }
                    }
                }

                changeItemBackground(position)
            }
        }
    }

    override fun getItemCount(): Int =
        tagsToShow?.let { it.visibleTagsCount + 2 } ?: 0

    fun update(result: TagsResult) {
        tagsToShow = result
        notifyDataSetChanged()
    }

    private fun View.changeItemBackground(position: Int) {
        if (position == selectedTagPosition) {
            elevation = 0f
            setBackgroundResource(R.drawable.bg_market_category_tag_selected_item)
        } else {
            elevation = context!!.resources.dipToPixels(6f)
            setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}