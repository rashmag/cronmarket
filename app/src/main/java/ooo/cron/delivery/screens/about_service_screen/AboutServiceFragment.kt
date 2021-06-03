package ooo.cron.delivery.screens.about_service_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.BaseFragment

/*
 * Created by Muhammad on 03.06.2021
 */



class AboutServiceFragment: BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_service, container, false)
    }
}