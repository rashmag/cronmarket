package ooo.cron.delivery.data.network

import ooo.cron.delivery.data.network.request.ConfirmCodeReq
import ooo.cron.delivery.data.network.request.SentCodeReq
import ooo.cron.delivery.data.network.request.SetUserNameReq
import ooo.cron.delivery.data.network.response.ConfirmCodeRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/*
 * Created by Muhammad on 28.04.2021
 */



interface RestService {

    @POST("Account/send_code")
    fun sentCode(@Body sentCodeReq: SentCodeReq): Call<Void>


    @POST("Account/confirm_code")
    fun sentConfirmCode(@Body sentConformCodeReq: ConfirmCodeReq): Call<ConfirmCodeRes>

    @POST("User/name")
    fun setUserName(@Header("Authorization") token: String, @Body name: SetUserNameReq): Call<Void>
}