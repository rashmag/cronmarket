package ooo.cron.delivery.screens.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity

/*
 * Created by Muhammad on 30.05.2021
 */



class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
            finish()
        }, 1000)
    }
}