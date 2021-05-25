package ooo.cron.delivery.screens.ordering_screen

import android.widget.EditText
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/*
 * Created by Muhammad on 19.05.2021
 */



interface OrderContract {

    interface View : MvpView {
        fun getBasketId()
        fun getPhone(phone: EditText)
        fun getAddress(address: String)
        fun getComment(comment: String)
        fun getDeliveryTime(deliveryTime: String)
        fun getEntrance(entrance: String)
        fun getFloor(floor:String)
        fun getFlat(flat:String)
        fun getOrderReq(): OrderReq
        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()
        fun showOrderSuccessfulScreen()
        fun showOrderErrorScreen()
    }

    interface Presenter : MvpPresenter<View> {
        fun sendOrder()
    }
}