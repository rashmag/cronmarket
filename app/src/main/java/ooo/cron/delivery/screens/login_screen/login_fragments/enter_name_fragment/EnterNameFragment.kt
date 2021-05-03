package ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_enter_name.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.utils.Constants
import ooo.cron.delivery.utils.Shared
import javax.inject.Inject

/*
 * Created by Muhammad on 28.04.2021
 */



class EnterNameFragment : Fragment(R.layout.fragment_enter_name), EnterNameContract.View {

    @Inject
    lateinit var presenter: EnterNamePresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        App.appComponent.enterNameComponentBuilder().build().inject(this)
        presenter.attachView(this)
        super.onViewCreated(view, savedInstanceState)
        onSetNameClick()
        initViews()
    }

    private fun initViews() {
        et_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btn_next.apply {
                    isEnabled = p0?.isNotEmpty()!!
                    isSelected = p0.isNotEmpty()
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        (activity as LoginActivity).hideBackButton()
    }

    private fun onSetNameClick() {
        btn_next.setOnClickListener {
            presenter.sentUserName()
        }
    }

    override fun getUserName(): String {
        return et_name.text.toString()
    }

    override fun getToken(): String {
        return "Bearer ${Shared.getStringValue(requireContext(), Constants.ACCESS_TOKEN)}"
    }

    override fun showError(parseError: String) {
        Toast.makeText(requireContext(), parseError, Toast.LENGTH_SHORT).show()
    }

    override fun cancelLogin() {
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}