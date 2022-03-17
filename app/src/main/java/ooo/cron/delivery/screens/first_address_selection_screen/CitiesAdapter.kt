package ooo.cron.delivery.screens.first_address_selection_screen

import android.content.Context
import android.widget.ArrayAdapter
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.City

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
class CitiesAdapter(context: Context) :
    ArrayAdapter<String>(context, R.layout.item_first_address_selection_city, mutableListOf()) {

    fun setData(cities: List<City>) {
        clear()
        cities.forEachIndexed { index, city ->
            insert(city.city, index)
        }
        notifyDataSetChanged()
    }
}