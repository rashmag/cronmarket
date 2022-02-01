package ooo.cron.delivery.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ooo.cron.delivery.screens.any_error_screen.AnyErrorActivity
import ooo.cron.delivery.screens.connection_error_screen.ConnectionErrorActivity

/**
 * Created by Maya Nasrueva on 19.01.2022
 * */

abstract class BaseFragment2 : Fragment() {

    abstract val baseViewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseViewModel.connectionErrorScreen.observe(viewLifecycleOwner, {
            showConnectionErrorScreen()
        })
        baseViewModel.anyErrorScreen.observe(viewLifecycleOwner, {
            showAnyErrorScreen()
        })
    }

    fun showAnyErrorScreen() {
        AnyErrorActivity.show(requireContext())
    }

    fun showConnectionErrorScreen() {
        ConnectionErrorActivity.show(requireContext())
    }
}