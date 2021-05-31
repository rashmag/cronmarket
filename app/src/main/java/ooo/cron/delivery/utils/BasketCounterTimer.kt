package ooo.cron.delivery.utils

import android.os.CountDownTimer
import android.util.Log

/**
 * Created by Ramazan Gadzhikadiev on 28.05.2021.
 */
class BasketCounterTimer : CountDownTimer(
    800,
    800
) {
    private var actionOnFinish = {}

    override fun onTick(millisUntilFinished: Long) {
        Log.d(this::class.simpleName, "Timer Restarted")
    }

    override fun onFinish() {
        actionOnFinish()
    }

    fun start(actionOnFinish: () -> Unit): CountDownTimer? {
        this.actionOnFinish = actionOnFinish
        return start()
    }
}