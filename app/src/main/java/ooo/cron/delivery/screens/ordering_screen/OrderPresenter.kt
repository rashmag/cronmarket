package ooo.cron.delivery.screens.ordering_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import javax.inject.Inject

/*
 * Created by Muhammad on 19.05.2021
 */



class OrderPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope
) : BaseMvpPresenter<OrderContract.View>(), OrderContract.Presenter {


    fun getBasketId(): String {
        return dataManager.readUserBasket()
    }

    override fun sendOrder() {
        mainScope.launch {
            withErrorsHandle(
                {
                    view?.let {

                        dataManager.sendOrder(
                            "Bearer ${dataManager.readToken().accessToken}",
                            getOrderReq()
                        ).handleOrderResponse()
                    }
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private fun getOrderReq(): OrderReq {
        var orderReq = view!!.getOrderReq()
        return orderReq.copy(
            deliverAtTime = if (orderReq.deliverAtTime?.isBlank() == true)
                null
            else orderReq.deliverAtTime
        )
    }

    private fun Response<ResponseBody>.handleOrderResponse() {
        if (isSuccessful) {
            view?.showOrderSuccessfulScreen()
        } else {
            view?.showOrderErrorScreen()
        }
    }

    fun getDeliveryCityId(): String? {
        return dataManager.readChosenCityId()
    }
}