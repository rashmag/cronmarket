package ooo.cron.delivery.screens

import androidx.fragment.app.Fragment
import ooo.cron.delivery.screens.any_error_screen.AnyErrorActivity
import ooo.cron.delivery.screens.connection_error_screen.ConnectionErrorActivity

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
abstract class BaseFragment : Fragment() {

    fun showAnyErrorScreen() {
        AnyErrorActivity.show(context!!)
    }

    fun showConnectionErrorScreen() {
        ConnectionErrorActivity.show(context!!)
    }
}