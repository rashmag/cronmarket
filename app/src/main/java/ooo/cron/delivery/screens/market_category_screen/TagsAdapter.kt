package ooo.cron.delivery.screens.market_category_screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.view.*
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Tag
import ooo.cron.delivery.data.network.models.TagsResult
import ooo.cron.delivery.utils.dipToPixels

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class TagsAdapter(
    private val onTagClickCallback: (tag: Tag) -> Unit,
    private val onAllTagsClickCallback: () -> Unit
) :
    RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private var tagsToShow: TagsResult? = null
    private var selectedTagPosition = 0

    private lateinit var popupMenu: PopupMenu

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ViewHolder.AllTagsViewHolder.VIEW_TYPE
            itemCount - 1 -> if (tagsToShow!!.tags.size > tagsToShow!!.visibleTagsCount)
                ViewHolder.OtherTagsViewHolder.VIEW_TYPE
            else
                ViewHolder.TagViewHolder.VIEW_TYPE
            else -> ViewHolder.TagViewHolder.VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder.createView(parent)
        return when (viewType) {
            ViewHolder.AllTagsViewHolder.VIEW_TYPE ->
                ViewHolder.AllTagsViewHolder(v).also {
                    with(it.itemView as TextView) {
                        text = context!!.getString(R.string.market_category_all_tags)
                        setOnClickListener {
                            selectedTagPosition = 0
                            notifyDataSetChanged()
                            onAllTagsClickCallback()
                        }
                    }
                }
            ViewHolder.OtherTagsViewHolder.VIEW_TYPE ->
                ViewHolder.OtherTagsViewHolder(v).also {
                    popupMenu = PopupMenu(it.itemView.context!!, it.itemView, Gravity.END)
                    with(it.itemView as TextView) {
                        text = context!!.getString(R.string.market_category_other_tags)
                        setOnClickListener {
                            showTagsPopup(context)
                        }
                    }
                }
            else ->
                ViewHolder.TagViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (holder is ViewHolder.TagViewHolder)
            tagsToShow?.let { tags ->
                (holder.itemView as TextView).apply {
                    val tagIndex = position - 1
                    if (tags.tags.isNotEmpty()) {
                        text = tags.tags[tagIndex].name
                        setOnClickListener {
                            selectedTagPosition = position
                            notifyDataSetChanged()
                            onTagClickCallback(tags.tags[tagIndex])
                        }
                    }
                }
            }

        holder.itemView.changeItemBackground(position)
    }

    override fun getItemCount(): Int =
        tagsToShow?.let {
            if (it.tags.isEmpty())
                return@let 0
            if (it.tags.size < it.visibleTagsCount)
                return@let it.tags.size + 1

            it.visibleTagsCount + 2
        } ?: 0

    fun update(result: TagsResult) {
        tagsToShow = result
        notifyDataSetChanged()
    }

    private fun showTagsPopup(context: Context) {
        if (popupMenu.menu.isNotEmpty())
            popupMenu.menu.clear()

        tagsToShow?.let { tags ->
            if (tags.visibleTagsCount < tags.tags.size) {
                tags.tags.subList(
                    tags.visibleTagsCount,
                    tags.tags.size
                )
            } else {
                tags.tags
            }.forEach { tag ->
                popupMenu.menu.add(tag.name)
            }

            if (selectedTagPosition >= itemCount && popupMenu.menu is MenuBuilder) {
                addCheckIconOnOtherTagSelected(context, tags.tags)
            }

            popupMenu.setOnMenuItemClickListener {
                val clickedTagIndex = tags.tags.indexOfFirst { tag -> tag.name == it.title }
                selectedTagPosition = itemCount + clickedTagIndex
                notifyDataSetChanged()
                onTagClickCallback(tags.tags[clickedTagIndex])
                true
            }

            popupMenu.show()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun addCheckIconOnOtherTagSelected(context: Context, tags: List<Tag>) {
        val menuBuilder = popupMenu.menu as MenuBuilder
        menuBuilder.setOptionalIconsVisible(true)
        menuBuilder.visibleItems.firstOrNull { menuItem ->
            menuItem.title == tags[selectedTagPosition - itemCount].name
        }?.let { item ->
            val iconMarginPx = context.resources.dipToPixels(12f).toInt()
            if (item.icon != null) {
                item.icon = InsetDrawable(
                    item.icon,
                    iconMarginPx,
                    0,
                    0,
                    0
                )
            } else {
                val iconDrawable = AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_market_category_tag_check
                )
                item.icon = object : InsetDrawable(
                    iconDrawable, iconMarginPx, 0, 0, 0
                ) {
                    override fun getIntrinsicWidth(): Int {
                        return iconMarginPx + iconMarginPx
                    }
                }
            }
        }
    }

    private fun View.changeItemBackground(position: Int) {
        if (position == selectedTagPosition ||
            (position == itemCount - 1 && selectedTagPosition >= itemCount)
        ) {
            if ((this as TextView).text == context.getString(R.string.market_category_other_tags)) {
                setBackgroundResource(R.drawable.bg_market_category_tag_other)
                this.setTextColor(resources.getColor(R.color.white))
            } else {
                setBackgroundResource(R.drawable.bg_market_category_tag_selected_item)
                this.setTextColor(resources.getColor(R.color.brand))
            }
        } else {
            setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
            elevation = 1f
            (this as TextView).setTextColor(resources.getColor(R.color.black))
            }
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class TagViewHolder(itemView: View) : ViewHolder(itemView) {
            companion object {
                const val VIEW_TYPE = 0
            }
        }

        class AllTagsViewHolder(itemView: View) : ViewHolder(itemView) {
            companion object {
                const val VIEW_TYPE = 1
            }
        }

        class OtherTagsViewHolder(itemView: View) : ViewHolder(itemView) {
            companion object {
                const val VIEW_TYPE = 2
            }
        }

        companion object {
            fun createView(parent: ViewGroup): View =
                LayoutInflater.from(parent.context!!)
                    .inflate(R.layout.item_market_category_tag, parent, false)
        }
    }
}