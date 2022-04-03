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
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.PopUpChooseDeliveryTimeBinding
import ooo.cron.delivery.utils.extensions.uiLazy
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.utils.extensions.setCustomTextColor
import ooo.cron.delivery.utils.extensions.withArgs
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import java.util.Calendar
import javax.inject.Inject

class OrderDeliveryTimeBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    private var _binding: PopUpChooseDeliveryTimeBinding? = null
    private val binding get() = _binding!!

    private var deliveryTimeLayoutManager: LinearLayoutManager? = null

    private var isMidnight = false

    private val adapterDeliveryTime by uiLazy {
        AdapterDeliveryTime { chosenTime ->
            addClickForDoneBtn(chosenTime)
        }
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

        val arrayTimeToday = arrayListOf<String>()

        val currentTime = Calendar.getInstance().time
        var hourNow = currentTime.hours
        var minuteNow = 0

        isMidnight = false

        // Проверка текущих минут
        // Н-р
        // Если сейчас 12:00 -> доступное время доставки будет 12:30
        // Если 12:20 -> доступное время доставки будет 13:00
        // Если сейчас 12:40 -> доступное время доставки будет 13:30

        when (currentTime.minutes) {
            0 -> minuteNow = MINUTE_STEP
            in 1..30 -> {
                minuteNow = 0
                hourNow++
            }
            in 31..39 -> {
                minuteNow = MINUTE_STEP
                hourNow++
            }
            in 40..60 -> {
                minuteNow = MINUTE_STEP
                hourNow++
            }
        }

        while (isMidnight.not()) {

            // Если текущее время < 10, то добавляем 0
            // Н-р если сейчас 9:00 -> то мы увидим 09:00

            val zeroHour = if (hourNow < 10) ZERO_TIME else EMPTY
            val zeroMinute = if (minuteNow < 10) ZERO_TIME else EMPTY

            if (hourNow > viewModel.getPartnerOpenHours()) {
                arrayTimeToday.add("$zeroHour$hourNow:$zeroMinute$minuteNow")
            }

            minuteNow += MINUTE_STEP

            if (minuteNow > 59) {
                minuteNow -= 60
                hourNow++
            }

            if (hourNow > viewModel.getPartnerCloseHours() - 1) {
                // Удаляю, потому что показывалось ненужное время
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

                if (parentPos != adapterDeliveryTime.selectedItem && parentPos != -1) {
                    adapterDeliveryTime.updateSelectedItem(parentPos.orZero())
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
                    setCustomTextColor(R.color.orange_ff4c30)

                    btnByTime.setBackgroundResource(R.drawable.btn_delivery_type_not_selected)
                    btnByTime.setCustomTextColor(R.color.black)

                    defaultTimeContainer.makeVisible()
                    scrollTimeContainer.makeGone()
                }
            }

            btnByTime.apply {
                setOnClickListener {
                    setBackgroundResource(R.drawable.bg_market_category_tag_not_selected_item)
                    setCustomTextColor(R.color.orange_ff4c30)

                    btnAsap.setBackgroundResource(R.drawable.btn_delivery_type_not_selected)
                    btnAsap.setCustomTextColor(R.color.black)

                    defaultTimeContainer.makeGone()
                    scrollTimeContainer.makeVisible()
                }
            }
        }
    }

    private fun addClickForDoneBtn(time: String?= "") {
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

        private const val MINUTE_STEP = 30
        private const val ZERO_TIME = "0"
        private const val EMPTY = ""
    }
}