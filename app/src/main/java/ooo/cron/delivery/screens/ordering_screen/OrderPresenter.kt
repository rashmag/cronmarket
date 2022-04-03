package ooo.cron.delivery.screens.ordering_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.Basket.Companion.deserializeDishes
import ooo.cron.delivery.data.network.request.OrderReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import ru.tinkoff.acquiring.sdk.localization.AsdkSource
import ru.tinkoff.acquiring.sdk.localization.Language
import ru.tinkoff.acquiring.sdk.models.DarkThemeMode
import ru.tinkoff.acquiring.sdk.models.Item
import ru.tinkoff.acquiring.sdk.models.Receipt
import ru.tinkoff.acquiring.sdk.models.enums.CheckType
import ru.tinkoff.acquiring.sdk.models.enums.Tax
import ru.tinkoff.acquiring.sdk.models.enums.Taxation
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions
import ru.tinkoff.acquiring.sdk.utils.Money
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.data.network.models.RefreshableToken

/*
 * Created by Muhammad on 19.05.2021
 */

class OrderPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
    private var basket: Basket?,
    private val analytics: BaseAnalytics
) : BaseMvpPresenter<OrderContract.View>(), OrderContract.Presenter {

    override fun onCreateMakingOrderScreen() {
        analytics.trackOpenMakingOrderScreen(dataManager.readUserPhone().toString())
    }

    override fun onCreateView() {

        mainScope.launch {
            withErrorsHandle(
                {
                    dataManager.getBasket(dataManager.readUserBasketId()).handleBasketResponse()
                    view?.hideProgress()
                },
                {
                    view?.showConnectionErrorScreen()
                },
                {
                    view?.showAnyErrorScreen()
                }
            )
        }
    }

    fun getBasketId(): String {
        return dataManager.readUserBasketId()
    }

    override fun paymentSucceed(paymentId: Long) {
        makeOrder(getPaidOrderRed(paymentId))
    }

    override fun paymentFailed() {
        view?.showOrderErrorScreen()
    }

    override fun sendOrder() {
        when (view?.getPaymentType()) {
            view?.getCashPaymentType() ->
                makeOrder(getOrderReq())
            view?.getCardPaymentType() ->
                onPaymentClick()
        }

        analytics.trackMakeOrderClick(
            phoneNumber = dataManager.readUserPhone().toString(),
            paymentType = view?.getPaymentType().orEmpty()
        )
    }

    fun getDeliveryCityId(): String? {
        return dataManager.readChosenCityId()
    }

    private fun makeOrder(orderReq: OrderReq?) {
        mainScope.launch {
            withErrorsHandle(
                {
                    view?.let {

                        if (orderReq != null) {
                            dataManager.sendOrder(
                                orderReq
                            ).handleOrderResponse()
                        }
                    }
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private fun getPaidOrderRed(paymentId: Long): OrderReq? {
        val orderReq = getOrderReq()
        return orderReq?.copy(
            comment = orderReq.comment +
                    "\n" +
                    " Номер платежа: $paymentId" +
                    "\n",
            paymentMethodId = 2
        )
    }

    private fun getOrderReq(): OrderReq? {
        val orderReq = view?.getOrderReq()
        return orderReq?.copy(
            deliverAtTime = if (orderReq.deliverAtTime?.isBlank() == true)
                null
            else orderReq.deliverAtTime,
        )
    }

    private fun onPaymentClick() {
        val paymentOptions = PaymentOptions().setOptions {

            orderOptions { // данные заказа
                orderId = paymentId()
                amount = Money.ofCoins(
                    paymentAmountInCoins()
                )
                title = "Заказ с доставкой в КРОН ДОСТАВКА"
                description = "Оплата товаров и услуги доставки."
                recurrentPayment = false
                receipt = Receipt(
                    ArrayList(receiptItems()),
                    "ramazan.gadjikadiev@gmail.com",
                    Taxation.USN_INCOME
                )
            }

            customerOptions { // данные покупателя
                customerKey = dataManager.readUserPhone()
                checkType = CheckType.NO.toString()
            }

            featuresOptions { // настройки визуального отображения и функций экрана оплаты
                useSecureKeyboard = true
                localizationSource = AsdkSource(Language.RU)
                handleCardListErrorInSdk = true
                darkThemeMode = DarkThemeMode.DISABLED
                emailRequired = false
            }
        }

        view?.openPaymentScreen(paymentOptions)
    }

    private fun Response<Basket>.handleBasketResponse() {
        if (isSuccessful) basket = body()!!
    }

    private suspend fun Response<ResponseBody>.handleOrderResponse() {

        val token = dataManager.readToken()

        if (code() == 401 && token.accessToken.isNotEmpty() && token.refreshToken.isNotEmpty()) {
            return dataManager.refreshToken(token)
                .handleRefreshToken()
        }

        if (isSuccessful) {
            view?.showOrderSuccessfulScreen()
        }
        else {
            view?.showOrderErrorScreen()
        }
    }

    private fun Response<RefreshableToken>.handleRefreshToken() {
        if (isSuccessful) {
            dataManager.writeToken(body()!!)
            return makeOrder(getOrderReq())
        }

        if (code() == 400 || code() == 401) {
            dataManager.removeToken()
        }

        view?.showAnyErrorScreen() ?: Unit
    }

    private fun paymentId() =
        Calendar.getInstance().timeInMillis.toString()

    private fun paymentAmountInCoins() = basket?.run {
        (amount + deliveryCost).inCoins()
    } ?: 0

    private fun receiptItems() =
        basket?.deserializeDishes()?.map {
            Item(
                it.name,
                it.cost.inCoins(),
                it.quantity.toDouble(),
                (it.cost * it.quantity).inCoins(),
                Tax.NONE
            )
        }?.toMutableList()?.apply {
            add(
                Item(
                    "Доставка",
                    basket?.deliveryCost?.inCoins(),
                    1.0,
                    basket?.deliveryCost?.inCoins(),
                    Tax.NONE
                )
            )
        }

    private fun Int.inCoins() =
        (this * 100).toLong()

    private fun Double.inCoins() =
        (this * 100).toLong()
}