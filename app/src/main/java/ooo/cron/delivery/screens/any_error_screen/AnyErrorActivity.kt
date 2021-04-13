package ooo.cron.delivery.screens.any_error_screen

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.base_error_screen.BaseErrorActivity

class AnyErrorActivity : BaseErrorActivity() {

    override fun onCreateErrorIcon(): Int = R.drawable.ic_any_error

    override fun onCreateErrorTitle(): CharSequence = getString(R.string.any_error_title)

    override fun onCreateErrorMessage(tvErrorMessage: TextView): CharSequence = SpannableString(
        getString(R.string.any_error_message)
    ).apply {
        val startClickableIndex = 94
        val endClickableIndex = 106
        setSpan(clickableSpan(), startClickableIndex, endClickableIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setSpan(foregroundSpan(), startClickableIndex, endClickableIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvErrorMessage.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onSubmitClicked(view: View) {
        TODO("Not yet implemented")
    }

    override fun onSubmitTitleCreated() = getString(R.string.any_error_submit_text)

    private fun clickableSpan() = object : ClickableSpan() {
        override fun onClick(widget: View) {
            TODO("Not yet implemented")
        }
    }

    private fun foregroundSpan() = ForegroundColorSpan(
        ContextCompat.getColor(this@AnyErrorActivity, R.color.brand)
    )
}