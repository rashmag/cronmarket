package ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment

import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.errors.ApiErrorsUtils
import ooo.cron.delivery.data.network.request.ConfirmCodeReq
import ooo.cron.delivery.data.network.request.SentCodeReq
import ooo.cron.delivery.data.network.response.ConfirmCodeRes
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
 * Created by Muhammad on 29.04.2021
 */



class ConfirmPhonePresenter @Inject constructor(
    private val dataManager: DataManager,
    private val apiErrorsUtils: ApiErrorsUtils
) :
    BaseMvpPresenter<ConfirmPhoneContract.View>(), ConfirmPhoneContract.Presenter {

    override fun sendConfirmCode() {
        dataManager.sentConfirmCode(ConfirmCodeReq(view?.getPhone()!!, view?.getCode()!!))
            .enqueue(object : Callback<ConfirmCodeRes> {
                override fun onFailure(call: Call<ConfirmCodeRes>, t: Throwable) {
                    //todo show error
                    println("conformcodethrowable $t")
                }

                override fun onResponse(
                    call: Call<ConfirmCodeRes>,
                    response: Response<ConfirmCodeRes>
                ) {
                    when {
                        response.isSuccessful -> {
                            view?.showNextScreen(response.body()!!)
                        }
                        response.code() == 400 -> {
                            view?.showError(response.errorBody()?.string()!!)

                        }
                        else -> {
                            view?.showError(apiErrorsUtils.parseError(response))
                        }
                    }
                }
            })
    }

    override fun sendPhone() {
        dataManager.sentCode(SentCodeReq(view?.getPhone()!!)).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                //todo show error
                println("sendcode error $t")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    view?.showError(apiErrorsUtils.parseError(response))
                }
            }
        })
    }
}