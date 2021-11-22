package ooo.cron.delivery.utils

import android.os.CountDownTimer
import android.util.Log

/**
 * Created by Ramazan Gadzhikadiev on 28.05.2021.
 */
class BasketCounterTimer : CountDownTimer(
    DEFAULT_MILLIS_IN_FUTURE,
    DEFAULT_COUNT_INTERVAL
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

    private companion object{
        private const val DEFAULT_MILLIS_IN_FUTURE = 0L
        private const val DEFAULT_COUNT_INTERVAL = 0L
    }
}