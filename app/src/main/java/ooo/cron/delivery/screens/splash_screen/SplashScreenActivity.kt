package ooo.cron.delivery.screens.splash_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.internal.button.DialogActionButton
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.main_screen.MainActivity
import ooo.cron.delivery.screens.onboard_screen.OnboardActivity
import javax.inject.Inject

/*
 * Created by Muhammad on 30.05.2021
 */



class SplashScreenActivity : BaseActivity(), SplashScreenContract.View {

    @Inject
    protected lateinit var presenter: SplashScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.splashScreenBuilder()
            .build()
            .inject(this)
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun navigateFirstAddressScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
            finish()
        }, 1000)
    }

    override fun navigateMainScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }

    override fun navigateGooglePlay() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    }

    override fun showUpdateVersionDialog() {
        val dialog = MaterialDialog(this)
            .title(R.string.attention_title)
            .message(R.string.version_update_title)
            .icon(R.drawable.ic_update)
            .positiveButton {
                presenter.updateAccepted()
            }
            .negativeButton {
                presenter.updateDeclined()
            }
            .cancelOnTouchOutside(false)
        val positiveButton = dialog.view.findViewById<DialogActionButton>(R.id.md_button_positive)
        positiveButton.text = "Обновить"
        positiveButton.isAllCaps = false
        positiveButton.updateTextColor(resources.getColor(R.color.errors_true))
        dialog.show()
    }

    override fun showOnboard() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnboardActivity::class.java))
            finish()
        }, 1000)
    }
}