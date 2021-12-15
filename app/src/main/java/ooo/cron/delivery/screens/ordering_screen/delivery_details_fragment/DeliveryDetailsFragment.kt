package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.redmadrobot.inputmask.MaskedTextChangedListener
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.FragmentDeliveryDetailsBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.ordering_screen.OrderContract
import ooo.cron.delivery.screens.ordering_screen.OrderingActivity
import java.util.*
import javax.inject.Inject

/*
 * Created by Muhammad on 18.05.2021
 */



class DeliveryDetailsFragment : BaseFragment(), DeliveryDetailsContract.View {

    private lateinit var orderingView: OrderContract.View

    @Inject
    lateinit var presenter: DeliveryDetailsPresenter

    @Inject
    lateinit var binding: FragmentDeliveryDetailsBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        orderingView = context as OrderContract.View
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
    }

    private fun injectDependencies() {
        App.appComponent.deliveryDetailsComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initScheduleTime()
    }

    private fun initScheduleTime() {
        with(requireActivity()) {
            PARTNER_OPEN_HOURS = intent.getIntExtra("PARTNER_OPEN_HOURS", -1)
            PARTNER_OPEN_MINUTES = intent.getIntExtra("PARTNER_OPEN_MINUTES", -1)
            PARTNER_CLOSE_HOURS = intent.getIntExtra("PARTNER_CLOSE_HOURS", -1)
            PARTNER_CLOSE_MINUTES = intent.getIntExtra("PARTNER_CLOSE_MINUTES", -1)
        }
    }


    override fun onStart() {
        super.onStart()
        binding.etAddress.apply {
            setText(presenter.getAddress())
            setOnClickListener {
                startActivity(
                    Intent(requireContext(), FirstAddressSelectionActivity::class.java)
                        .putExtra(IS_FROM_ORDERING, true)
                )
            }
        }
    }

    private fun initViews() {
        onChooseDeliveryTimeClick()

        binding.run {
            etPhone.addTextChangedListener(
                MaskedTextChangedListener(
                    "+7 ([000]) [000]-[00]-[00]",
                    true, etPhone, null, null
                )
            )
            etPhone.setText(presenter.getPhone())
        }
    }

    private fun onChooseDeliveryTimeClick() {
        with(binding) {
            rbInTime.setOnCheckedChangeListener { _, b ->
                vgChooseDeliveryTime.visibility =
                    if (b) View.VISIBLE else View.GONE

                nestedScrollView.post {
                    nestedScrollView.fullScroll(View.FOCUS_DOWN)
                }
                etDeliveryTime.setOnClickListener {
                    showTimePickerDialog()
                }
            }
        }
    }


    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val openCalendar = Calendar.getInstance()
        openCalendar.set(Calendar.HOUR_OF_DAY, PARTNER_OPEN_HOURS + 1)
        openCalendar.set(Calendar.MINUTE, PARTNER_OPEN_MINUTES)

        val closeCalendar = Calendar.getInstance()
        closeCalendar.set(Calendar.HOUR_OF_DAY, PARTNER_CLOSE_HOURS - 1)
        closeCalendar.set(Calendar.MINUTE, PARTNER_CLOSE_MINUTES)

        TimePickerDialog(
            context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val chooseCalendar = Calendar.getInstance()
                chooseCalendar.set(Calendar.HOUR_OF_DAY, hour)
                chooseCalendar.set(Calendar.MINUTE, minute)

                val hourAfterCurrentCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY) + 1)
                }

                val isOpenTimeError = openCalendar.after(chooseCalendar)
                val isHourAfterCurrentTimeError = hourAfterCurrentCalendar.after(chooseCalendar)
                val isCLoseTimeError = closeCalendar.before(chooseCalendar)

                if (isOpenTimeError ||
                    isCLoseTimeError ||
                    isHourAfterCurrentTimeError
                ) {
                    binding.etDeliveryTime.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_error)
                    binding.tvError.apply {
                        text =
                            requireContext().resources.getString(R.string.delivery_time_error_title)
                        visibility = View.VISIBLE
                        orderingView.setOrderButtonEnabled(false)
                    }
                } else {
                    binding.etDeliveryTime.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_edit_text_selector
                    )
                    binding.tvError.visibility = View.INVISIBLE
                    orderingView.setOrderButtonEnabled(true)
                }

                val deliveryTime = String.format("%d:%s", hour, checkDigit(minute))
                binding.etDeliveryTime.setText(deliveryTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    /**
     * TimePicker возвращает целое число. Если мы выберем время 12:05,
     * TimePicker вернет 12:5
     * делаем проверку, что если число от 0 до  9, то перед числом добавляем 0
     */
    private fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }

    fun getDeliveryInfo() {
        binding.run {
            with(orderingView) {
                if (presenter.getAddress()!!.isEmpty()) {
                    etAddress.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_error)
                    (activity as OrderingActivity).isRequestParametersValid = false
                    return@run
                }

                etAddress.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_selector)
                (activity as OrderingActivity).isRequestParametersValid = false
                if (etPhone.text.isEmpty() || etPhone.text.toString() == "+7 (") {
                    etPhone.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_error)
                    (activity as OrderingActivity).isRequestParametersValid = false
                    return@run
                }
                getAddress(presenter.getAddress()!!)
                getEntrance(etEntrance.text.toString())
                getFloor(etFloor.text.toString())
                getFlat(etFlat.text.toString())
                getPhone(etPhone)
                getComment(etComments.text.toString())
                getDeliveryTime(etDeliveryTime.text.toString())
                getBasketId()
                (activity as OrderingActivity).isRequestParametersValid = true
            }
        }

    }

    companion object {
        var PARTNER_OPEN_HOURS = -1
        var PARTNER_OPEN_MINUTES = -1
        var PARTNER_CLOSE_HOURS = -1
        var PARTNER_CLOSE_MINUTES = -1

        const val IS_FROM_ORDERING = "IS_FROM_ORDERING"
    }
}