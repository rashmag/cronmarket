package ooo.cron.delivery.utils.section_recycler_view

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ooo.cron.delivery.R

abstract class SectionRecyclerViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

    private val mSectionTitle by lazy { mView.findViewById<TextView>(R.id.tv_section_title) }
    private val mSectionList by lazy { mView.findViewById<RecyclerView>(R.id.rv_section_list) }

    fun bindSectionTitle(title: String, position: Int) {
        Log.d("test2","title = " + title)
        mSectionTitle.text = title

        bindSectionListAdapter(mSectionList.also {
            it.layoutManager = GridLayoutManager(mView.context, 2)
        }, position)
    }

    abstract fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int)

}