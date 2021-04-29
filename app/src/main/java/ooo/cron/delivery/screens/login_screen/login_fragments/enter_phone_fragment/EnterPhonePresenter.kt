package ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment

import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.errors.ApiErrorsUtils
import ooo.cron.delivery.data.network.request.SentCodeReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
 * Created by Muhammad on 28.04.2021
 */



class EnterPhonePresenter @Inject constructor(
    private val dataManager: DataManager,
    private val apiErrorsUtils: ApiErrorsUtils
) : BaseMvpPresenter<EnterPhoneContract.View>(),
    EnterPhoneContract.Presenter {


    override fun sendPhone() {
        dataManager.sentCode(SentCodeReq(view?.getPhone()!!)).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                //todo show error
                println("sendcode error $t")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    view?.startNextScreen()
                } else {
                    view?.showError(apiErrorsUtils.parseError(response))
                }
            }
        })
    }
}