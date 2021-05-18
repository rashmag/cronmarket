package ooo.cron.delivery.screens.partners_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes

/*
 * Created by Muhammad on 17.05.2021
 */



class AdditiveArrayAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val additives: List<PartnerProductsRes.Additives>
) : ArrayAdapter<PartnerProductsRes.Additives>(context, layoutResource, additives) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_additive, parent, false)
        view.findViewById<TextView>(R.id.tv_additive).text = additives[position].name
        return view
    }

    override fun getItem(position: Int): PartnerProductsRes.Additives? {
        return additives[position]
    }
}