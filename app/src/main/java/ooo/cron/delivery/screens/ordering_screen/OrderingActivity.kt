package ooo.cron.delivery.screens.ordering_screen

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_ordering.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.databinding.ActivityOrderingBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment.DeliveryDetailsFragment
import javax.inject.Inject

/*
 * Created by Muhammad on 18.05.2021
 */



class OrderingActivity : BaseActivity(), OrderContract.View {

    @Inject
    lateinit var presenter: OrderPresenter

    @Inject
    lateinit var binding: ActivityOrderingBinding

    private val orderReq = OrderReq()

    override fun onCreate(savedInstanceState: Bundle?) {
        initDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewPager()
        onOrderClick()
    }

    private fun onOrderClick() {
        binding.btnOrder.setOnClickListener {
            presenter.sendOrder()
        }
    }

    private fun initDependencies() {
        App.appComponent.orderingComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
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

    override fun getBasketId() {
        orderReq.basketId = presenter.getBasketId()
    }

    override fun getPhone(phone: String) {
        orderReq.phoneNumber = phone
    }

    override fun getEntrance(entrance: String) {
        orderReq.entrance = entrance
    }

    override fun getFloor(floor: String) {
        orderReq.floor = floor
    }

    override fun getFlat(flat: String) {
        orderReq.flat = flat
    }

    override fun getAddress(address: String) {
        orderReq.address = address
    }

    override fun getComment(comment: String) {
        orderReq.comment = comment
    }

    override fun getDeliveryTime(deliveryTime: String) {
        orderReq.deliverAtTime = deliveryTime
    }

    override fun getOrderReq(): OrderReq {
        applyChanges()
        return orderReq
    }

    private fun applyChanges() {
        val deliveryFragment = supportFragmentManager.findFragmentByTag("f" + 0)
        (deliveryFragment as DeliveryDetailsFragment).getDeliveryInfo()
    }


    override fun showOrderSuccessfulScreen() {

    }

    override fun showOrderErrorScreen() {
        TODO("Not yet implemented")
    }
}