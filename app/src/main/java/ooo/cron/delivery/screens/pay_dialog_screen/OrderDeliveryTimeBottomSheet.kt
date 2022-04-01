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
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.PopUpChooseDeliveryTimeBinding
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.utils.extensions.uiLazy
import javax.inject.Inject

class OrderDeliveryTimeBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    private val adapterDeliveryTime by uiLazy {
        AdapterDeliveryTime()
    }

    private var _binding: PopUpChooseDeliveryTimeBinding ?= null
    private val binding get() = _binding!!

    private var deliveryTimeLayoutManager: LinearLayoutManager ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PopUpChooseDeliveryTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClicksForDeliveryTypesBtn()
        initDeliveryTimeAdapter()
        initDeliveryTimeRecyclerScrollListener()
    }

    private fun initDeliveryTimeAdapter(){
        with(binding.recyclerDeliveryTime) {
            val timeList = listOf("11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00")

            LinearSnapHelper().attachToRecyclerView(this@with)
            deliveryTimeLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            layoutManager = deliveryTimeLayoutManager
            adapter = adapterDeliveryTime
            adapterDeliveryTime.setTime(timeList)
        }
    }

    private fun initDeliveryTimeRecyclerScrollListener(){
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

    private fun addClicksForDeliveryTypesBtn(){
        with(binding){
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
}