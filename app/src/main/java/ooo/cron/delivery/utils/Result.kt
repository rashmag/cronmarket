package ooo.cron.delivery.utils

import java.lang.Exception

sealed class Result<out Any> ()
data class Success<T>(val body: T): Result<T>()
data class Error(var e: Exception? = null, var message: String? = null): Result<Nothing>()
object NoConnection: Result<Nothing>()