package ooo.cron.delivery.data.network.errors

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

/*
 * Created by Muhammad on 26.04.2021
 */



class ApiErrorsUtils {

    fun parseError(response: Response<*>): String {
        return try {
            val jObjError = JSONObject(response.errorBody()?.string()!!)
            jObjError.getString("title")
        } catch (e: Exception) {
            println("Ошибка парсинга ошибки сервера : $e").toString()
        }
    }
}