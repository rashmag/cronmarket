package ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.dialog_privacy_policy.*
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.utils.Utils
import javax.inject.Inject

/*
 * Created by Muhammad on 28.04.2021
 */



class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone), EnterPhoneContract.View {

    @Inject
    lateinit var presenter: EnterPhonePresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        App.appComponent.enterPhoneComponentBuilder().build().inject(this)
        presenter.attachView(this)
        super.onViewCreated(view, savedInstanceState)
        initPhoneTextWatcher()
        onNextClick()
        onPrivacyPolicyClick()
    }

    private fun onPrivacyPolicyClick() {
        tv_privacy_policy.setOnClickListener {
            val bottomSheetPrivacyPolicyDialog =
                BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView =
                LayoutInflater.from(context).inflate(R.layout.dialog_privacy_policy, bottom_sheet)
            bottomSheetView.findViewById<Button>(R.id.btn_close).setOnClickListener {
                bottomSheetPrivacyPolicyDialog.dismiss()
            }
            bottomSheetPrivacyPolicyDialog.setContentView(bottomSheetView)
            bottomSheetPrivacyPolicyDialog.show()
        }
    }

    private fun onNextClick() {
        btn_next.setOnClickListener {
            Utils.hideKeyboard(requireActivity())
            presenter.sendPhone()
        }
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


    override fun getPhone(): String {
        return Utils.phoneReplace(et_phone)
    }

    override fun showError(message: String) {
        tv_error.apply {
            visibility = View.VISIBLE
            text = message
        }
        et_phone.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_text_error)
    }

    override fun startNextScreen() {
        (activity as LoginActivity).setViewPagerPosition(1)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}