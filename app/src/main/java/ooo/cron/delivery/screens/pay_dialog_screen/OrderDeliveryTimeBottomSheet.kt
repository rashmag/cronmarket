package ooo.cron.delivery.screens.pay_dialog_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.ZonedDateTime
import javax.inject.Inject
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.PopUpChooseDeliveryTimeBinding
import ooo.cron.delivery.di.screens.order_pay.OrderViewModelFactory
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.utils.extensions.setCustomTextColor
import ooo.cron.delivery.utils.extensions.uiLazy
import ooo.cron.delivery.utils.extensions.withArgs

class OrderDeliveryTimeBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    private var _binding: PopUpChooseDeliveryTimeBinding? = null
    private val binding get() = _binding!!

    private var deliveryTimeLayoutManager: LinearLayoutManager? = null

    private var currentTime: ZonedDateTime? = null
    var hourNow = -1

    private var partnerOpenTime = 0
    private var partnerCloseTime = 0

    private val partnerIsOpen: Boolean by uiLazy {
        requireArguments().getBoolean(IS_OPEN)
    }

    private val adapterDeliveryTime by uiLazy {
        AdapterDeliveryTime { chosenTime ->
            addClickForDoneBtn(chosenTime)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        currentTime = ZonedDateTime.now()
        hourNow = currentTime?.hour.orZero()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PopUpChooseDeliveryTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        partnerOpenTime = viewModel.getPartnerOpenHours()
        partnerCloseTime = viewModel.getPartnerCloseHours()

        checkBtnDeliveryTypeAvailable()
        initDeliveryTimeAdapter()
        initDeliveryTimeRecyclerScrollListener()
        addClickForDoneBtn()
    }

    private fun initDeliveryTimeAdapter() {
        with(binding.recyclerDeliveryTime) {

            LinearSnapHelper().attachToRecyclerView(this@with)
            deliveryTimeLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            layoutManager = deliveryTimeLayoutManager
            adapter = adapterDeliveryTime
            adapterDeliveryTime.setTime(viewModel.generateDeliveryTimeInterval())
        }
    }

    private fun initDeliveryTimeRecyclerScrollListener() {
        binding.recyclerDeliveryTime.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val parentPos = deliveryTimeLayoutManager?.findLastCompletelyVisibleItemPosition()

                if (parentPos != adapterDeliveryTime.selectedItem && parentPos != NOT_SELECTED_ITEM) {
                    adapterDeliveryTime.updateSelectedItem(parentPos.orZero())
                }
            }
        })
    }

    private fun addClicksForDeliveryTypesBtn() {
        with(binding) {
            btnAsap.setOnClickListener {
                enableBtnAsap()
                disableBtnByTime()
            }

            btnByTime.setOnClickListener {
                enableBtnByTime()
                disableBtnAsap()
            }
        }
    }

    private fun addClickForDoneBtn(time: String? = "") {
        with(binding) {
            btnDone.setOnClickListener {
                if (time?.isEmpty() == true) {
                    viewModel.setDeliveryTime(getString(R.string.delivery_details_delivery_time_title))
                } else {
                    viewModel.setDeliveryTime(time.orEmpty())
                }
                dismiss()
            }
        }
    }

    private fun checkBtnDeliveryTypeAvailable() {
        if (partnerIsOpen) {
            if (hourNow >= partnerCloseTime - 1) {
                if (hourNow < partnerCloseTime) {
                    enableBtnAsap()
                    disableBtnByTime()
                } else {
                    disableBtnAsap()
                    enableBtnByTime()
                }
            } else {
                if (hourNow > partnerOpenTime) {
                    addClicksForDeliveryTypesBtn()
                } else {
                    disableBtnAsap()
                    enableBtnByTime()
                }
            }
        } else {
            disableBtnAsap()
            enableBtnByTime()
        }
    }

    private fun enableBtnAsap() {

        with(binding) {
            btnAsap.setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
            btnAsap.setCustomTextColor(R.color.orange_ff4c30)
            defaultTimeContainer.makeVisible()
        }
    }

    private fun disableBtnAsap() {
        with(binding) {
            btnAsap.setBackgroundResource(R.drawable.btn_delivery_type_not_selected)
            btnAsap.setCustomTextColor(R.color.black)
            defaultTimeContainer.makeGone()
        }
    }

    private fun enableBtnByTime() {
        with(binding) {
            btnByTime.setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
            btnByTime.setCustomTextColor(R.color.orange_ff4c30)
            scrollTimeContainer.makeVisible()
        }
    }

    private fun disableBtnByTime() {
        with(binding) {
            btnByTime.setBackgroundResource(R.drawable.btn_delivery_type_not_selected)
            btnByTime.setCustomTextColor(R.color.black)
            scrollTimeContainer.makeGone()
        }
    }

    private fun injectDependencies() {
        App.appComponent.orderComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }

    companion object {
        private const val NOT_SELECTED_ITEM = -1
        private const val IS_OPEN = "IS_OPEN"

        fun newInstance(isOpen: Boolean) = OrderDeliveryTimeBottomSheet().withArgs {
            putBoolean(IS_OPEN, isOpen)
        }
    }
}