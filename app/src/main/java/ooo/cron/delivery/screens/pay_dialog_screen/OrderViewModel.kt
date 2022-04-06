package ooo.cron.delivery.screens.pay_dialog_screen


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.OrderInteractor
import ooo.cron.delivery.data.network.SPrefsService
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.Basket.Companion.deserializeDishes
import ooo.cron.delivery.data.network.models.PayData
import ooo.cron.delivery.utils.SingleLiveEvent
import ru.tinkoff.acquiring.sdk.models.Item
import ru.tinkoff.acquiring.sdk.models.Receipt
import ru.tinkoff.acquiring.sdk.models.enums.Tax
import ru.tinkoff.acquiring.sdk.models.enums.Taxation
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderViewModel @Inject constructor(
    private val interactor: OrderInteractor
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { context, exception ->
        Log.e("exception", exception.toString())
    }
    val cardPayData: SingleLiveEvent<PayData> = SingleLiveEvent()
    val googlePayData: SingleLiveEvent<Pair<PayData, String>> = SingleLiveEvent()
    val callingPayInfoDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    val callingDeliveryCostInfo: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val paymentStatus: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val gPayClick: SingleLiveEvent<Long> = SingleLiveEvent()
    private val mutablePhone: MutableLiveData<String> = MutableLiveData()
    val phone: LiveData<String> get() = mutablePhone
    private val mutableBasketState: MutableLiveData<BasketState> = MutableLiveData()
    val basketState: LiveData<BasketState> get() = mutableBasketState
    private val _commentTextLivedata = MutableLiveData("")
    val commentTextLiveData: LiveData<String> = _commentTextLivedata
    private val mutablePayVariantState: MutableLiveData<PaymentVariant> = MutableLiveData()
    val payVariantState: LiveData<PaymentVariant> get() = mutablePayVariantState

    private val mutableDeliveryTime: MutableLiveData<String> = MutableLiveData()
    val deliveryTime: LiveData<String> get() = mutableDeliveryTime

    init {
        Log.e("app", "inited")
    }

    fun onViewCreated() = viewModelScope.launch {
        if (interactor.getDeliveryCityId() == KHAS_ID)
            callingDeliveryCostInfo.postValue(true)
        else callingDeliveryCostInfo.postValue(false)
        loadBasket()
    }

    fun onPayClicked() {
        when (payVariantState.value) {
            is CardVariant -> onCardPaySelected()
            is CashVariant -> onPaymentSuccess() // никуда лезть не надо, сразу же подтверждаем заказ и отправляем запрос на сервер
            is GPayVariant -> onGooglePaySelected()
            else -> callingPayInfoDialog.call()
        }
    }

    fun onCardPaySelected() {
        cardPayData.postValue(getOrderInfo())
    }

    fun onGooglePaySelected() {
        gPayClick.postValue(getOrderInfo().amountSum.toLong())
    }

    fun onGooglePayResultSuccess(token: String) {
        googlePayData.postValue(Pair(getOrderInfo() ,token))
    }

    private fun getOrderInfo(): PayData {
        val phone = interactor.getPhone().toString()
        val basket = interactor.getBasket()
        val payData = basket?.let {
            PayData(amountSum = it.amount + it.deliveryCost, receipt = Receipt(
                ArrayList(receiptsItems(it)),
                "cron.devsystems@gmail.com",
                Taxation.USN_INCOME
            ), phone = phone)
        }
        return payData as PayData
    }

    fun receiptsItems(basket: Basket) =
        basket.deserializeDishes().map {
            Item(
                it.name,
                it.cost.inCoins(),
                it.quantity.toDouble(),
                (it.cost * it.quantity).inCoins(),
                Tax.NONE
            )
        }.toMutableList().apply {
            add(
                Item(
                    "Доставка",
                    (basket.deliveryCost).inCoins(),
                    1.0,
                    basket.deliveryCost.inCoins(),
                    Tax.NONE
                )
            )
        }

    private fun loadBasket() {
        mutableBasketState.postValue(Loading)
        viewModelScope.launch(handler) {
            try {
                val basket = interactor.getBasket()
                mutableBasketState.postValue(basket?.let {
                    Default(it)
                })
            } catch (e: Exception) {
                mutableBasketState.postValue(Error)
            }
        }
    }

    fun setPayVariant(variant: PaymentVariant) {
        mutablePayVariantState.postValue(variant)
    }

    fun setComment(comment: String) {
        _commentTextLivedata.postValue(comment)
    }

    //метод для POST-запроса на отправку заказа после оплаты(если картой или GPay)/выбора налички
    //TODO обработать ошибку отсутствия интернета при успешной оплате и отправке POST-запроса

    fun onPaymentSuccess() {
        viewModelScope.launch {
            val paymentMethod = if (payVariantState.value == CashVariant) 1 else 2
            val comment = commentTextLiveData.value.toString()
            val basket = interactor.getBasket()
            basket?.let {
                val basketClearReq = interactor.getBasketClearReq(it)
                interactor.clearBasket(basketClearReq)
            }
            interactor.sendOrder(
                paymentMethod = paymentMethod,
                comment = comment,
                address = getAddress(),
                deliveryAtTime = deliveryTime.value.orEmpty()
            )
        }
        paymentStatus.postValue(true)
    }

    //метод для вызова показа ошибки в экране корзины
    fun onPaymentFailed() {
        paymentStatus.postValue(false)
    }

    fun getAddress(): String{
        return interactor.readAddress()
    }

    fun setDeliveryTime(time: String){
        mutableDeliveryTime.postValue(time)
    }

    fun getPartnerOpenHours(): Int {
        return interactor.getPartnerOpenHours()
    }

    fun getPartnerCloseHours(): Int{
        return interactor.getPartnerCloseHours()
    }

    private fun Int.inCoins() =
        (this * 100).toLong()

    private fun Double.inCoins() =
        (this * 100).toLong()

    companion object {
        const val KHAS_ID = "2d0c08eb-da25-4afa-8de2-db70a29a9520"
    }
}