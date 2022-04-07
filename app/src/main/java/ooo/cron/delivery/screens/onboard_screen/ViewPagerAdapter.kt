package ooo.cron.delivery.screens.onboard_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ooo.cron.delivery.screens.onboard_screen.fragments.FourFragment
import ooo.cron.delivery.screens.onboard_screen.fragments.OneFragment
import ooo.cron.delivery.screens.onboard_screen.fragments.ThreeFragment
import ooo.cron.delivery.screens.onboard_screen.fragments.TwoFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return COUNT_ONBOARD_FRAGMENTS
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OneFragment()
            1 -> TwoFragment()
            2 -> ThreeFragment()
            3 -> FourFragment()
            else -> OneFragment()
        }
    }

    companion object{
        const val COUNT_ONBOARD_FRAGMENTS = 4
    }
}