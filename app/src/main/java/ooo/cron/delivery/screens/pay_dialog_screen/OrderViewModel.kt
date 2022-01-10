package ooo.cron.delivery.screens.pay_dialog_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.OrderInteractor
import ooo.cron.delivery.data.OrderPrefsRepository
import ooo.cron.delivery.data.OrderRestRepository
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.Basket.Companion.deserializeDishes
import ooo.cron.delivery.data.network.models.PayData
import ooo.cron.delivery.data.network.request.OrderReq
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
    val payData: SingleLiveEvent<PayData> = SingleLiveEvent()
    val callingDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    val paymentStatus: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val mutablePhone: MutableLiveData<String> = MutableLiveData()
    val phone: LiveData<String> get() = mutablePhone
    private val mutableBasketState: MutableLiveData<BasketState> = MutableLiveData()
    val basketState: LiveData<BasketState> get() = mutableBasketState
    private val _commentTextLivedata = MutableLiveData("")
    val commentTextLiveData: LiveData<String> = _commentTextLivedata
    private val mutablePayVariantState: MutableLiveData<PaymentVariant> = MutableLiveData()
    val payVariantState: LiveData<PaymentVariant> get() = mutablePayVariantState

    init {
        Log.e("app", "inited")
    }

    fun onCreateView() = viewModelScope.launch {
        loadBasket()
    }

    fun onPayClicked() {
        when (payVariantState.value) {
            is CardVariant -> getOrderInfo()
            else -> callingDialog.call()
        }
    }

    private fun getOrderInfo() {
        val phone = interactor.getPhone().toString()
        val basket = interactor.getBasket()
        val amountSum =
            basket.amount + basket.deliveryCost
        val receipt = Receipt(
            ArrayList(receiptsItems(basket)),
            "cron.devsystems@gmail.com",
            Taxation.USN_INCOME
        )
        payData.postValue(PayData(phone, amountSum, receipt))
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
                mutableBasketState.postValue(Default(basket))
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
    //TODO !!!!!!!!!! обработать ошибку отсутствия интернета при успешной оплате и отправке POST-запроса

    fun onPaymentSuccess() {
        //TODO сформировать OrderReq с введенных юзером данных
        viewModelScope.launch {
            val paymentMethod = if (payVariantState.value == CashVariant) 1 else 2
            interactor.sendOrder(paymentMethod)
        }
        paymentStatus.postValue(true)
    }

    //метод для вызова показа ошибки в экране корзины
    fun onPaymentFailed() {
        /*TODO вызвать SingleLiveEvent для показа ошибки оплаты заказа*/
        paymentStatus.postValue(false)
    }

    private fun Int.inCoins() =
        (this * 100).toLong()

    private fun Double.inCoins() =
        (this * 100).toLong()
}