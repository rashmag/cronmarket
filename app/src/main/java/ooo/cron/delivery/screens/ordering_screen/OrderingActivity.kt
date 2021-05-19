package ooo.cron.delivery.screens.ordering_screen

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_ordering.*
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.ActivityOrderingBinding
import ooo.cron.delivery.screens.BaseActivity
import javax.inject.Inject

/*
 * Created by Muhammad on 18.05.2021
 */



class OrderingActivity : BaseActivity() {

//    @Inject
//    lateinit var binding: ActivityOrderingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordering)
        initViewPager()
    }

    private fun initViewPager() {
        vp_ordering.adapter = OrderingViewPagerAdapter(this)


        TabLayoutMediator(tl_ordering, vp_ordering) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.delivery_details_title)
                1 -> getString(R.string.order_costs_title)
                else -> ""
            }
        }.attach()
    }
}