package ooo.cron.delivery.screens

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ooo.cron.delivery.screens.any_error_screen.AnyErrorActivity
import ooo.cron.delivery.screens.base_mvp.ConnectionErrorActivity

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            window.statusBarColor = Color.BLACK
    }

    fun showAnyErrorScreen() {
        AnyErrorActivity.show(this)
    }

    fun showConnectionErrorScreen() {
        ConnectionErrorActivity.show(this)
    }
}