package ooo.cron.delivery.screens.ordering_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment.DeliveryDetailsFragment
import ooo.cron.delivery.screens.ordering_screen.order_cost_fragment.OrderCostFragment

/*
 * Created by Muhammad on 18.05.2021
 */



class OrderingViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        const val FRAGMENT_COUNT = 2
    }

    override fun getItemCount() = FRAGMENT_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DeliveryDetailsFragment()
            1 -> OrderCostFragment()
            else -> Fragment()
        }
    }
}