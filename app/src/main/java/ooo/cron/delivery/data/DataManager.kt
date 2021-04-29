package ooo.cron.delivery.data

import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.request.ConfirmCodeReq
import ooo.cron.delivery.data.network.request.SentCodeReq
import ooo.cron.delivery.data.network.request.SetUserNameReq
import ooo.cron.delivery.data.network.response.ConfirmCodeRes
import retrofit2.Call
import javax.inject.Inject

/*
 * Created by Muhammad on 28.04.2021
 */



class DataManager @Inject constructor(private val restService: RestService) {


    fun sentCode(sentCodeReq: SentCodeReq): Call<Void> {
        return restService.sentCode(sentCodeReq)
    }


    fun sentConfirmCode(confirmCodeReq: ConfirmCodeReq): Call<ConfirmCodeRes> {
        return restService.sentConfirmCode(confirmCodeReq)
    }

    fun setUserName(token: String,userName: SetUserNameReq): Call<Void> {
        return restService.setUserName(token,userName)
    }
}