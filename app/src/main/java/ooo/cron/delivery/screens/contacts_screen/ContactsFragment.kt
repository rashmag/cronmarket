package ooo.cron.delivery.screens.contacts_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.BaseFragment

/*
 * Created by Muhammad on 02.06.2021
 */



class ContactsFragment: BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }
}