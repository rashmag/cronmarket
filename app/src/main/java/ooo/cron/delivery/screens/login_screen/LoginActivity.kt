package ooo.cron.delivery.screens.login_screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment.ConfirmPhoneFragment
import ooo.cron.delivery.screens.main_screen.MainActivity

/*
 * Created by Muhammad on 26.04.2021
 */



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewPager.adapter = LoginViewPagerAdapter(supportFragmentManager)
        onBackClick()
    }

    private fun onBackClick() {
        iv_back.setOnClickListener {
            if (viewPager.currentItem != 0) {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            } else {
                onBackPressed()
            }
        }
    }

    fun hideBackButton() {
        iv_back.visibility = View.GONE
    }

    fun setViewPagerPosition(position: Int) {
            viewPager.post {
                viewPager.setCurrentItem(position, true)
            }
    }
}