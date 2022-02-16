package ooo.cron.delivery.screens.order_history_detail_screen.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Named
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.OrderHistoryDetailDish
import ooo.cron.delivery.data.network.models.OrderHistoryDetailNetModel
import ooo.cron.delivery.databinding.FragmentOrderHistoryDetailBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryFragment
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeInvisible
import ooo.cron.delivery.utils.extensions.makeVisible
import ooo.cron.delivery.utils.extensions.setBold
import ooo.cron.delivery.utils.extensions.uiLazy
import ooo.cron.delivery.utils.extensions.withArgs
import ooo.cron.delivery.utils.itemdecoration.OrderHistoryDetailItemDecoration
import java.text.SimpleDateFormat

class OrderHistoryDetailFragment : BaseFragment() {

    private var _binding: FragmentOrderHistoryDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    @Named("Detail")
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<OrderHistoryDetailViewModel> { viewModelFactory }

    var orderId = ""

    private val orderHistoryDetailAdapter by uiLazy {
        OrderHistoryDetailAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .orderHistoryDetailComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)

        orderId = arguments?.getString(ARG_ORDER_ID, "").orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getOrderHistoryDetail(orderId)
        setupViewModel()
        initAdapter()
        onBackPressed()
    }

    private fun setupViewModel() {
        with(viewModel) {
            orderHistoryDetail.observe(viewLifecycleOwner) {
                it.body()?.let { detailModel ->
                    showInfo(detailModel)
                }
            }

            orderHistoryDetailList.observe(viewLifecycleOwner) {
                showList(it)
            }
        }
    }

    private fun showList(list: List<OrderHistoryDetailDish>) {
        orderHistoryDetailAdapter.submitList(list)
    }

    private fun showInfo(model: OrderHistoryDetailNetModel) {
        with(binding) {
            if (model.statusName == ORDER_CANCELLED) {
                stepperContainer.makeInvisible()
                orderCancelledContainer.makeVisible()
            }

            partnerName.text = model.partnerName

            val input = SimpleDateFormat(BACKEND_DATE_FORMAT)
            val output = SimpleDateFormat(NECESSARY_FORMAT)
            val date = input.parse(model.dateTime)
            orderDate.text = output.format(date)

            orderDeliveryTo.text = getString(R.string.order_history_screen_delivery_to_title, model.deliverAtTime)
            orderAddress.text = model.deliveryLocation
            orderCancelledText.text = model.feedbackModerationStatus
            stepperIndicator.apply {
                currentStep = model.statusId
                setIndicatorColor(Color.parseColor("#00875A"))
            }

            when (model.statusId) {
                1 -> titleModeration.setBold()
                2 -> titleProcess.setBold()
                3 -> titleWay.setBold()
                4 -> titleDone.setBold()
            }

            ordersPrice.text = getString(R.string.order_history_screen_total_amount_ruble, model.amount.toString())
            orderDelivery.text =
                getString(R.string.order_history_screen_total_amount_ruble, model.deliveryCost.toString())
            orderDiscount.text = getString(R.string.order_history_screen_total_amount_ruble, model.discount.toString())
            totalSum.text = getString(R.string.order_history_screen_total_amount_ruble, model.totalAmount.toString())
        }
    }

    private fun initAdapter() {
        with(binding.recyclerOrderHistoryDetail) {
            adapter = orderHistoryDetailAdapter
            addItemDecoration(
                OrderHistoryDetailItemDecoration(
                    MARGIN_SPACING_VALUE_16,
                    MARGIN_SPACING_VALUE_100
                )
            )
        }
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    childFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    ).replace(R.id.order_history_detail_container, OrderHistoryFragment())
                        .addToBackStack(null)
                        .commit()

                    with(binding) {
                        allContainer.makeGone()
                        totalPriceContainer.makeGone()
                    }

                    isEnabled = false
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ORDER_ID = "ARG_ORDER_ID"
        fun newInstance(
            orderId: String
        ) = OrderHistoryDetailFragment().withArgs {
            putString(ARG_ORDER_ID, orderId)
        }

        private const val ORDER_CANCELLED = "Отменен"

        const val MARGIN_SPACING_VALUE_16 = 16
        const val MARGIN_SPACING_VALUE_100 = 100

        private const val BACKEND_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        private const val NECESSARY_FORMAT = "dd.MM.yy HH:mm"
    }
}