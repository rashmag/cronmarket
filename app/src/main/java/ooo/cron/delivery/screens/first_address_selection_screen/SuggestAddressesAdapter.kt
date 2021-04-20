package ooo.cron.delivery.screens.first_address_selection_screen

import android.content.Context
import android.widget.ArrayAdapter
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.SuggestAddress

/**
 * Created by Ramazan Gadzhikadiev on 15.04.2021.
 */
class SuggestAddressesAdapter(context: Context, suggestAddresses: List<SuggestAddress>) :
    ArrayAdapter<SuggestAddress>(context, R.layout.item_first_address_selection_address, suggestAddresses)