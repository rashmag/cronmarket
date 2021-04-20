package ooo.cron.delivery.screens.first_address_selection_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.City

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
class CitiesAdapter(context: Context) :
    ArrayAdapter<City>(context, R.layout.item_first_address_selection_city, mutableListOf())