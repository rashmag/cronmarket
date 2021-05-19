package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.BaseFragment

/*
 * Created by Muhammad on 18.05.2021
 */



class DeliveryDetailsFragment: BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delivery_details, container, false)
    }
}