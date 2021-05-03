package ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_confirm_phone.*
import kotlinx.android.synthetic.main.fragment_confirm_phone.btn_next
import kotlinx.android.synthetic.main.fragment_confirm_phone.tv_error
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.utils.Utils
import javax.inject.Inject

/*
 * Created by Muhammad on 28.04.2021
 */



class ConfirmPhoneFragment : Fragment(R.layout.fragment_confirm_phone), ConfirmPhoneContract.View {

    @Inject
    lateinit var presenter: ConfirmPhonePresenter

    private lateinit var countDownTimer: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        App.appComponent.confirmPhoneComponentBuilder().build().inject(this)
        presenter.attachView(this)
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        initSendPhoneText()
        initCountDownTimer()
    }

    private fun initViews() {
        btn_next.setOnClickListener {
            Utils.hideKeyboard(requireActivity())
            presenter.sendConfirmCode()
        }


        et_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btn_next.apply {
                    isEnabled = p0?.length == 6
                    isSelected = p0?.length == 6
                }
            }
        })

    }

    private fun initSendPhoneText() {
        tv_on_phone_send_code.text = String.format(
            requireContext().getString(R.string.sms_sent_title),
            presenter.getUserPhone()
        )
    }

    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                tv_timer.apply {
                    text = getString(R.string.enter_phone_repeat_send_code_title)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.main_dark))
                    setOnClickListener {
                        initCountDownTimer()
                        presenter.sendPhone()
                    }
                    isClickable = true
                }

            }

            override fun onTick(p0: Long) {
                tv_timer.apply {
                    text = String.format(
                        requireContext().getString(R.string.repeat_sent_code_title),
                        p0 / 1000
                    )
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.grey90))
                    isClickable = false


                }
            }
        }
        countDownTimer.start()
    }


    override fun onPause() {
        super.onPause()
        countDownTimer.cancel()
    }


    override fun getCode(): String {
        return et_code.text.toString()
    }

    override fun showNextScreen() {
        (activity as LoginActivity).setViewPagerPosition(2)
    }

    override fun showError(message: String) {
        tv_error.apply {
            visibility = View.VISIBLE
            text = message
        }
        et_code.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_error)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}