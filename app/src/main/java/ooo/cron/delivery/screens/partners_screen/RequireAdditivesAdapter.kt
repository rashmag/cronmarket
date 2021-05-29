package ooo.cron.delivery.screens.partners_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.fajaragungpramana.sectionrecyclerview.SectionRecyclerViewAdapter
import com.github.fajaragungpramana.sectionrecyclerview.SectionRecyclerViewHolder
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.RequireAdditiveModel
import ooo.cron.delivery.databinding.ItemRequireAdditivesBinding

/*
 * Created by Muhammad on 29.05.2021
 */



class RequireAdditivesAdapter(
    private val requireAdditivesList: ArrayList<RequireAdditiveModel>,
    private val listener: AdditivesAdapter.OnRequireAdditivesListener
) : SectionRecyclerViewAdapter<RequireAdditivesAdapter.ViewHolder, RequireAdditiveModel>(
    requireAdditivesList
) {

    override fun viewHolder(view: View) = ViewHolder(view)


    inner class ViewHolder(view: View) : SectionRecyclerViewHolder(view) {

        override fun bindSectionListAdapter(recyclerView: RecyclerView, position: Int) {
            recyclerView.adapter =
                AdditivesAdapter(requireAdditivesList[position].additiveList, listener)
        }

    }

}


class AdditivesAdapter(
    private val requireAdditivesList: List<PartnerProductsRes.Additive>,
    private val listener: OnRequireAdditivesListener
) : RecyclerView.Adapter<AdditivesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_require_additives, parent, false)
        )
    }

    override fun getItemCount() = requireAdditivesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAdditives(requireAdditivesList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val binding = ItemRequireAdditivesBinding.bind(itemView)

        fun bindAdditives(requiredAdditiveGroups: PartnerProductsRes.Additive) {
            binding.run {
                with(requiredAdditiveGroups) {
                    rbAdditives.apply {
                        text = name
                        setOnClickListener {
                            requiredAdditiveGroups.isChecked = !requiredAdditiveGroups.isChecked
                            isChecked = requiredAdditiveGroups.isChecked
                        }
                    }
                }
            }
        }
    }

    interface OnRequireAdditivesListener
}

