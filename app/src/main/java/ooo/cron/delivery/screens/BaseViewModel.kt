package ooo.cron.delivery.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import ooo.cron.delivery.utils.SingleLiveEvent
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Maya Nasrueva on 18.01.2021
 * */

abstract class BaseViewModel: ViewModel() {
    open val connectionErrorScreen: SingleLiveEvent<Unit> = SingleLiveEvent()
    open val anyErrorScreen: SingleLiveEvent<Unit> = SingleLiveEvent()
}