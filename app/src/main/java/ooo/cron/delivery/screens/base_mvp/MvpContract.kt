package ooo.cron.delivery.screens.base_mvp

import android.util.Log
import kotlinx.coroutines.CancellationException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

interface MvpView

interface MvpPresenter<V : MvpView> {
    fun attachView(view: V)
    fun detachView()
}

abstract class BaseMvpPresenter<V : MvpView> : MvpPresenter<V> {
    protected var view: V? = null

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    protected suspend fun withErrorsHandle(
        action: suspend () -> Unit,
        onConnectionError: (() -> Unit)? = null,
        onAnyError: () -> Unit
    ) {
        try {
            action()
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException, is SocketTimeoutException ->
                    onConnectionError?.invoke()
                is CancellationException -> showJobCancellationLogMessage(e)
                else ->
                    onAnyError()
            }
        }
    }

    private fun showJobCancellationLogMessage(e: CancellationException) {
        Log.d(this.javaClass.name, e.message ?: "")
    }
}