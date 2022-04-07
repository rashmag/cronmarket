package ooo.cron.delivery.screens.onboard_screen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.base.BaseFragment


class FourFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_four, container, false)
    }

}