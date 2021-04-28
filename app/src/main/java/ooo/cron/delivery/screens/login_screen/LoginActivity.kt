package ooo.cron.delivery.screens.login_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import ooo.cron.delivery.R

/*
 * Created by Muhammad on 26.04.2021
 */



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewPager.adapter = LoginViewPagerAdapter(supportFragmentManager)
    }
}