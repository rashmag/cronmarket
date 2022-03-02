package ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment

import android.util.Log
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.errors.ApiErrorsUtils
import ooo.cron.delivery.data.network.models.RefreshableToken
import ooo.cron.delivery.data.network.request.ConfirmCodeReq
import ooo.cron.delivery.data.network.request.SentCodeReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.network.models.UserResponse

/*
 * Created by Muhammad on 29.04.2021
 */

class ConfirmPhonePresenter @Inject constructor(
    private val dataManager: DataManager,
    private val apiErrorsUtils: ApiErrorsUtils,
    private val mainScope: CoroutineScope
) :
    BaseMvpPresenter<ConfirmPhoneContract.View>(), ConfirmPhoneContract.Presenter {

    override fun detachView() {
        super.detachView()
        mainScope.cancel()
    }

    private fun showAuthorizeUser(response: Response<UserResponse>) {
        val user: UserResponse? = response.body()

        user?.let {
            view?.showAuthorizedUser(it.user.name)
        }
    }
    override fun sendConfirmCode() {
        val basketId = dataManager.readUserBasketId()
        dataManager.sentConfirmCode(
            ConfirmCodeReq(
                dataManager.readUserPhone()!!,
                view?.getCode()!!,
                basketId
            )
        ).enqueue(object : Callback<RefreshableToken> {
            override fun onFailure(call: Call<RefreshableToken>, t: Throwable) {
                //todo show error
                println("conformcodethrowable $t")
            }

            override fun onResponse(
                call: Call<RefreshableToken>,
                response: Response<RefreshableToken>
            ) {

                when {
                    response.isSuccessful -> {

                        mainScope.launch {

                            dataManager.writeToken(response.body()!!)
                            val userResponse = dataManager.getUser("Bearer ${response.body()!!.accessToken}")
                            if (userResponse.isSuccessful) {
                                dataManager.writeUserBasketId(
                                    userResponse.body()?.basket?.id ?: DataManager.EMPTY_UUID
                                )
                                showAuthorizeUser(userResponse)
                            }else{
                                view?.showNextScreen(ConfirmPhoneFragment.ENTER_NAME_FRAGMENT)
                            }
                        }
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
        dataManager.sentCode(SentCodeReq(dataManager.readUserPhone()!!))
            .enqueue(object : Callback<Void> {
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

    override fun getUserPhone() =
        dataManager.readUserPhone()
}