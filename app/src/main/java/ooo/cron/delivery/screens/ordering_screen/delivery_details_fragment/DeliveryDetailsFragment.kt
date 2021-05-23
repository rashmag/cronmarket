package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import android.app.TimePickerDialog
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
import java.util.*
import javax.inject.Inject

/*
 * Created by Muhammad on 18.05.2021
 */



class DeliveryDetailsFragment : BaseFragment(), DeliveryDetailsContract.View {

    @Inject
    lateinit var presenter: DeliveryDetailsPresenter

    @Inject
    lateinit var binding: FragmentDeliveryDetailsBinding

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
    }

    private fun initViews() {
        onChooseDeliveryTimeClick()

        binding.run {
            etAddress.apply {
                setText(presenter.getAddress())
                setOnClickListener {
                    startActivity(
                        Intent(requireContext(), FirstAddressSelectionActivity::class.java)
                            .putExtra("fromOrderingActivity", true)
                    )
                }
            }
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
        val openTime = 9
        val closeTime = 23

        val openCalendar = Calendar.getInstance()
        openCalendar.set(Calendar.HOUR_OF_DAY, openTime)

        val closeCalendar = Calendar.getInstance()
        closeCalendar.set(Calendar.HOUR_OF_DAY, closeTime - 1)
        closeCalendar.set(Calendar.MINUTE, 30)

        val timePicker = TimePickerDialog(
            context, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                val chooseCalendar = Calendar.getInstance()
                chooseCalendar.set(Calendar.HOUR_OF_DAY, hour)
                chooseCalendar.set(Calendar.MINUTE, minute)

                if (openCalendar.after(chooseCalendar) || closeCalendar.before(chooseCalendar)) {
                    binding.etDeliveryTime.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_error)
                    binding.tvError.apply {
                        text =
                            requireContext().resources.getString(R.string.delivery_time_error_title)
                        visibility = View.VISIBLE
                    }
                } else {
                    binding.etDeliveryTime.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_edit_text_selector
                    )
                    binding.tvError.visibility = View.INVISIBLE
                }

                binding.etDeliveryTime.setText("$hour:${checkDigit(minute)}")
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    /**
     * TimePicker возвращает целое число. Если мы выберем время 12:05,
     * TimePicker вернет 12:5
     * делаем проверку, что если число от 0 до  9, то перед числом добавляем 0
     */
    private fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }
}