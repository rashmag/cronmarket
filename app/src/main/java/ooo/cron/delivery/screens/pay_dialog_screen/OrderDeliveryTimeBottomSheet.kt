package ooo.cron.delivery.screens.pay_dialog_screen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.PopUpChooseDeliveryTimeBinding
import ooo.cron.delivery.utils.extensions.*
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import java.util.*
import javax.inject.Inject

class OrderDeliveryTimeBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    private val adapterDeliveryTime by uiLazy {
        AdapterDeliveryTime { chosenTime ->
            addClickForDoneBtn(chosenTime)
        }
    }

    private var _binding: PopUpChooseDeliveryTimeBinding? = null
    private val binding get() = _binding!!

    private var deliveryTimeLayoutManager: LinearLayoutManager? = null

    private var isMidnight = false

    private val startTime by uiLazy {
        requireArguments().getString(ARG_START_TIME).orEmpty()
    }

    private val endTime by uiLazy {
        requireArguments().getString(ARG_END_TIME).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PopUpChooseDeliveryTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.partnerWorkStartTime.observe(viewLifecycleOwner){
//            startTime = it
//        }
//
//        viewModel.partnerWorkEndTime.observe(viewLifecycleOwner){
//            endTime = it
//        }

        addClicksForDeliveryTypesBtn()
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
            adapterDeliveryTime.setTime(getTimeToday())
        }
    }

    private fun getTimeToday(): ArrayList<String> {

        val arrayTimeToday = ArrayList<String>()

        val stepMinute = 30
//        var iteration = 0

        isMidnight = false

        val currentTime = Calendar.getInstance().time
        var hourNow = currentTime.hours
        var minuteNow = 0

        when (currentTime.minutes) {
            in 0..15 -> minuteNow = 0
            in 16..39 -> minuteNow = 30
            in 40..60 -> {
                minuteNow = 0
                hourNow++
            }
        }

        while (isMidnight.not()) {

            val zeroHour = if (hourNow < 10) "0" else ""
            val zeroMinute = if (minuteNow < 10) "0" else ""

            if (hourNow > startTime.toInt()) {
                arrayTimeToday.add("$zeroHour$hourNow:$zeroMinute$minuteNow")
            }

//            iteration++
//
//            if (iteration > 5) stepMinute = 60

            minuteNow += stepMinute

            if (minuteNow > 59) {
                minuteNow -= 60
                hourNow++
            }

//            if(hourNow < startTime.toInt() + 1){
//                hourNow = startTime.toInt()
//            }

            if (hourNow > endTime.toInt() - 1) {
//                minuteNow = 0
                arrayTimeToday.removeLast()
                isMidnight = true
            }
        }

        return arrayTimeToday
    }

    private fun initDeliveryTimeRecyclerScrollListener() {
        binding.recyclerDeliveryTime.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val parentPos = deliveryTimeLayoutManager?.findLastCompletelyVisibleItemPosition()

                if (parentPos != adapterDeliveryTime.parentItem && parentPos != -1) {

                    adapterDeliveryTime.parentItem = parentPos.orZero()

                    adapterDeliveryTime.notifyDataSetChanged()
                }
            }
        })
    }

    private fun addClicksForDeliveryTypesBtn() {
        with(binding) {
            btnAsap.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
                    setTextColor(Color.parseColor("#FF4C30"))

                    btnByTime.setBackgroundResource(R.drawable.btn_delivery_type_not_selected)
                    btnByTime.setTextColor(Color.parseColor("#000000"))

                    defaultTimeContainer.makeVisible()
                    scrollTimeContainer.makeGone()
                }
            }

            btnByTime.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
                    setTextColor(Color.parseColor("#FF4C30"))

                    btnAsap.setBackgroundResource(R.drawable.btn_delivery_type_not_selected)
                    btnAsap.setTextColor(Color.parseColor("#000000"))

                    defaultTimeContainer.makeGone()
                    scrollTimeContainer.makeVisible()
                }
            }
        }
    }

    private fun addClickForDoneBtn(time: String ?= "") {
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

    private fun injectDependencies() {
        App.appComponent.orderComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }

    companion object {

        const val ARG_START_TIME = "ARG_START_TIME"
        const val ARG_END_TIME = "ARG_END_TIME"

        fun newInstance(startTime: String, endTime: String) = OrderDeliveryTimeBottomSheet().withArgs {
            putString(ARG_START_TIME, startTime)
            putString(ARG_END_TIME, endTime)
        }
    }
}