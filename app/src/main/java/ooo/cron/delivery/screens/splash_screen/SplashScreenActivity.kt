package ooo.cron.delivery.screens.splash_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.main_screen.MainActivity
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
        UpdateVersionDialog(
            presenter::updateDeclined,
            presenter::updateAccepted
        ).show(supportFragmentManager, this::class.simpleName)
    }
}