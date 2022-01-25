package ooo.cron.delivery.screens

import androidx.lifecycle.ViewModel
import ooo.cron.delivery.utils.*

/**
 * Created by Maya Nasrueva on 18.01.2021
 * */

abstract class BaseViewModel: ViewModel() {
    val connectionErrorScreen: SingleLiveEvent<Unit> = SingleLiveEvent()
    val anyErrorScreen: SingleLiveEvent<Unit> = SingleLiveEvent()

    protected fun <T> Result<T>.process(onSuccess: (T) -> Unit) {
        return when (this) {
            is Success -> onSuccess.invoke(body)
            is Error -> anyErrorScreen.call()
            is NoConnection -> connectionErrorScreen.call()
        }
    }
}