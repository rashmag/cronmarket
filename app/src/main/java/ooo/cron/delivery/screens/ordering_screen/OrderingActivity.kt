package ooo.cron.delivery.screens.ordering_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.databinding.ActivityOrderingBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.basket_screen.BasketActivity
import ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment.DeliveryDetailsFragment
import ooo.cron.delivery.screens.ordering_screen.order_cost_fragment.OrderCostFragment
import ooo.cron.delivery.utils.Utils
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions
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

    var isRequestParametersValid: Boolean = false

    private var basketModel: Basket?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        basketModel = intent.getParcelableExtra(BASKET_MODEL)

        initDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewPager()
        onBackClick()
        onOrderClick()
    }

    override fun onStart() {
        super.onStart()
        presenter.onCreateView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            presenter.paymentSucceed(
                data!!.getLongExtra(
                    TinkoffAcquiring.EXTRA_PAYMENT_ID, -1L
                )
            )
        }

        if (resultCode == TinkoffAcquiring.RESULT_ERROR) {
            presenter.paymentFailed()
        }
    }

    private fun onOrderClick() {
        binding.btnOrder.apply {
            text = getString(R.string.order_title)
            setOnClickListener {
                applyChanges()
                if (isRequestParametersValid)
                    if (binding.vpOrdering.currentItem == 0) {
                        binding.vpOrdering.setCurrentItem(1, true)
                    } else {
                        presenter.sendOrder()
                    }
            }

        }
    }

    private fun initDependencies() {
        basketModel?.let {
            App.appComponent.orderingComponentBuilder()
                .buildInstance(layoutInflater)
                .basketModel(it)
                .build()
                .inject(this)
        }
    }

    private fun initViewPager() {
        binding.vpOrdering.adapter = OrderingViewPagerAdapter(this)


        TabLayoutMediator(binding.tlOrdering, binding.vpOrdering) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.delivery_details_title)
                1 -> getString(R.string.order_costs_title)
                else -> ""
            }
        }.attach()
    }

    private fun onBackClick() =
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    override fun getBasketId() {
        orderReq.basketId = presenter.getBasketId()
    }

    override fun getPhone(phone: EditText) {
        orderReq.phoneNumber = Utils.phoneReplace(phone)
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

    override fun setOrderButtonEnabled(isEnable: Boolean) {
        binding.btnOrder.isEnabled = isEnable
    }

    private fun applyChanges() {
        orderReq.saveAddress = false
        orderReq.discount = 0
        orderReq.deliveryCityId = presenter.getDeliveryCityId()
        val deliveryFragment = supportFragmentManager.findFragmentByTag("f" + 0)
        (deliveryFragment as DeliveryDetailsFragment).getDeliveryInfo()
    }

    override fun showOrderSuccessfulScreen() {
        binding.run {
            vgOrderPayStatus.visibility = View.VISIBLE
            ivOrderStatus.setImageDrawable(
                ContextCompat.getDrawable(
                    this@OrderingActivity,
                    R.drawable.ic_circle_done
                )
            )
            tvOrderStatus.text = getString(R.string.ordering_pay_status_done)
            tvOrderDetailsStatus.text = getString(R.string.ordering_pay_status_done_details_title)
            btnOrder.text = getString(R.string.done_title)
            btnOrder.setOnClickListener {
                onBackPressed()
            }

            BasketActivity.stopActivity()
        }
    }

    override fun showOrderErrorScreen() {
        binding.run {
            vgOrderPayStatus.visibility = View.VISIBLE
            ivOrderStatus.setImageDrawable(
                ContextCompat.getDrawable(
                    this@OrderingActivity,
                    R.drawable.ic_circle_cross
                )
            )
            tvOrderStatus.text = getString(R.string.ordering_pay_status_error)
            tvOrderDetailsStatus.text = getString(R.string.ordering_pay_status_error_detail_title)
            btnOrder.text = getString(R.string.order_repeat_title)
            btnOrder.setOnClickListener {
                vgOrderPayStatus.visibility = View.GONE
                onOrderClick()
            }
        }
    }

    override fun hideProgress() {
        binding.vgProgress.root.visibility = View.GONE
    }

    override fun getPaymentType(): String =
        (supportFragmentManager.findFragmentByTag("f" + 1) as OrderCostFragment)
            .getPaymentType()

    override fun getCashPaymentType(): String =
        resources.getStringArray(R.array.payment_method_array)[CASH_PAYMENT_TYPE_INDEX]

    override fun getCardPaymentType(): String =
        resources.getStringArray(R.array.payment_method_array)[CARD_PAYMENT_TYPE_INDEX]

    override fun openPaymentScreen(paymentOptions: PaymentOptions) {
        TinkoffAcquiring(
            getString(R.string.tinkoff_terminal_key),
            getString(R.string.tinkoff_terminal_password),
            getString(R.string.tinkoff_terminal_public_key)
        ).openPaymentScreen(
            this,
            paymentOptions,
            TINKOFF_PAYMENT_REQUEST_CODE
        )
    }

    companion object {
        private const val TINKOFF_PAYMENT_REQUEST_CODE = 1000
        private const val CASH_PAYMENT_TYPE_INDEX = 0
        private const val CARD_PAYMENT_TYPE_INDEX = 1

        private const val BASKET_MODEL = "BASKET_MODEL"
    }
}