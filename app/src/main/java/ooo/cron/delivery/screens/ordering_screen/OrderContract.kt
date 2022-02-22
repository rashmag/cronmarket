package ooo.cron.delivery.screens.ordering_screen

import android.widget.EditText
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions

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
        fun getFloor(floor: String)
        fun getFlat(flat: String)
        fun getOrderReq(): OrderReq
        fun showOrderSuccessfulScreen()
        fun showOrderErrorScreen()
        fun setOrderButtonEnabled(isEnable: Boolean)

        fun hideProgress()

        fun getPaymentType(): String

        fun getCashPaymentType(): String
        fun getCardPaymentType(): String

        fun openPaymentScreen(paymentOptions: PaymentOptions)

        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()
    }

    interface Presenter : MvpPresenter<View> {
        fun onCreateMakingOrderScreen()
        fun onCreateView()
        fun paymentSucceed(paymentId: Long)
        fun paymentFailed()
        fun sendOrder()
    }
}