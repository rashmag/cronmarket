package ooo.cron.delivery.screens

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

    protected suspend fun ErrorHandler(
        action: suspend() -> Unit,
        onConnectionError: (() -> Unit)? = null,
        onAnyError: () -> Unit
    ) {
        try {
            action()
        } catch (e:Exception) {
            when (e) {
                is UnknownHostException, is SocketTimeoutException -> {
                    onConnectionError?.invoke()
                }
                else -> {
                    onAnyError()
                }
            }
        }
    }
}