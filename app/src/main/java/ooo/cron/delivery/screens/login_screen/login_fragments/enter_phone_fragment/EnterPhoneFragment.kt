package ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import ooo.cron.delivery.R

/*
 * Created by Muhammad on 28.04.2021
 */



class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhoneTextWatcher()

    }

    private fun initPhoneTextWatcher() {
        et_phone.addTextChangedListener(object : MaskedTextChangedListener(
            "+7 ([000]) [000]-[00]-[00]",
            true, et_phone, null, null
        ) {
            override fun onTextChanged(
                text: CharSequence,
                cursorPosition: Int,
                before: Int,
                count: Int
            ) {
                super.onTextChanged(text, cursorPosition, before, count)
                btn_next.apply {
                    isEnabled = text.length == 18
                    isSelected = text.length == 18
                }
            }
        })

    }


}