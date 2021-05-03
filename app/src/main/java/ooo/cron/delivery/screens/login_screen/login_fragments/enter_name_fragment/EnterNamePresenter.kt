package ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment

import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.errors.ApiErrorsUtils
import ooo.cron.delivery.data.network.request.SetUserNameReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
 * Created by Muhammad on 29.04.2021
 */



class EnterNamePresenter @Inject constructor(
    private val dataManager: DataManager,
    private val apiErrorsUtils: ApiErrorsUtils
) :
    BaseMvpPresenter<EnterNameContract.View>(), EnterNameContract.Presenter {

    override fun sentUserName() {
        dataManager.setUserName(
            accessTokenParameter(),
            SetUserNameReq(view?.getUserName()!!)
        )
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    //todo show error
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        view?.cancelLogin()
                    } else {
                        view?.showError(apiErrorsUtils.parseError(response))
                    }
                }
            })
    }

    private fun accessTokenParameter() =
        "Bearer ${dataManager.readToken().accessToken}"
}