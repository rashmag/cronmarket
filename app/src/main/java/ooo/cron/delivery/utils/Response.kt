package ooo.cron.delivery.utils

import okhttp3.ResponseBody
import java.lang.Exception

sealed class Response<out Any>
object SuccessResponse: Response<ResponseBody>()
data class ErrorResponse(var e: Exception? = null, var message: String? = null): Response<ResponseBody>()