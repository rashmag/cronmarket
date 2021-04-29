package ooo.cron.delivery.data.network.errors

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

/*
 * Created by Muhammad on 26.04.2021
 */



class ApiErrorsUtils @Inject constructor() {

    fun parseError(response: Response<*>): String {
        return try {
            val jObjError = JSONObject(response.errorBody()?.string()!!)
            val jsonArray: JSONArray = jObjError.getJSONArray("validationErrors")
            val jsonObject = jsonArray.getJSONObject(0)
            val jsonArray2: JSONArray = jsonObject.getJSONArray("errorMessages")
            jsonArray2.getString(0)
        } catch (e: Exception) {
            println("Ошибка парсинга ошибки сервера : $e").toString()
        }
    }
}