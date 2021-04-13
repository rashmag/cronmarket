package ooo.cron.delivery.screens.base_mvp

import android.view.View
import android.widget.TextView
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.base_error_screen.BaseErrorActivity

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
class ConnectionErrorActivity : BaseErrorActivity() {
    override fun onCreateErrorIcon(): Int =
        R.drawable.ic_connection_error

    override fun onCreateErrorTitle(): CharSequence =
        getString(R.string.connection_error_title)

    override fun onCreateErrorMessage(tvErrorMessage: TextView) =
        getString(R.string.connection_error_message)

    override fun onSubmitTitleCreated() =
        getString(R.string.connection_error_submit_text)

    override fun onSubmitClicked(view: View) {
        finish()
    }
}