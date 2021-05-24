package ooo.cron.delivery.screens.ordering_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ooo.cron.delivery.data.DataManager
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
        //todo get basket id
        return "basket_id"
    }

    override fun sendOrder() {
        mainScope.launch {
            withErrorsHandle(
                { dataManager.sendOrder(view?.getOrderReq()!!).handleOrderResponse() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }


    private fun Response<ResponseBody>.handleOrderResponse() {
        if (isSuccessful) {
            view?.showOrderSuccessfulScreen()
        } else {
            view?.showOrderErrorScreen()
        }
    }
}