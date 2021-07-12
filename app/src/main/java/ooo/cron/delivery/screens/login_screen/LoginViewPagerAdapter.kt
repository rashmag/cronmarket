package ooo.cron.delivery.screens.login_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment.ConfirmPhoneFragment
import ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment.EnterNameFragment
import ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment.EnterPhoneFragment

/*
 * Created by Muhammad on 28.04.2021
 */



class LoginViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EnterPhoneFragment()
            1 -> ConfirmPhoneFragment()
            2 -> EnterNameFragment()
            else -> Fragment()
        }
    }

    override fun getCount() = 3

}