package ooo.cron.delivery.screens.base_error_screen

import android.os.Bundle
import android.view.View
import android.widget.TextView
import ooo.cron.delivery.App
import ooo.cron.delivery.databinding.ActivityBaseErrorBinding
import ooo.cron.delivery.screens.BaseActivity
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 12.04.2021.
 */
abstract class BaseErrorActivity : BaseActivity() {

    @Inject
    lateinit var binding: ActivityBaseErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.baseErrorComponentBuilder()
            .layoutInflater(layoutInflater)
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            ivBaseError.setImageResource(onCreateErrorIcon())
            btnBaseErrorSubmit.text = onSubmitTitleCreated()
            btnBaseErrorSubmit.setOnClickListener { onSubmitClicked(it) }
            tvBaseErrorTitle.text = onCreateErrorTitle()
            tvBaseErrorMessage.text = onCreateErrorMessage(tvBaseErrorMessage)
        }
    }

    abstract fun onCreateErrorIcon(): Int
    abstract fun onCreateErrorTitle(): CharSequence
    abstract fun onCreateErrorMessage(tvErrorMessage: TextView): CharSequence
    abstract fun onSubmitTitleCreated(): CharSequence
    abstract fun onSubmitClicked(view: View)
}