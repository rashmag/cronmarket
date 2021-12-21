package ooo.cron.delivery.screens.pay_dialog_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.OrderPrefsRepository
import ooo.cron.delivery.data.OrderRestRepository
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
    private val restRepo: OrderRestRepository,
    private val prefsRepo: OrderPrefsRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { context, exception ->
        Log.e("exception", exception.toString())
    }
    val payData: SingleLiveEvent<PayData> = SingleLiveEvent()
    val callingDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
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
        val phone = prefsRepo.readUserPhone().toString()
        val basket = prefsRepo.readBasket()
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
                    basket.deliveryCost.inCoins(),
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
                val basket = prefsRepo.readBasket()
                Log.d("basket", basket.toString())
                mutableBasketState.postValue(Default(basket))
            } catch (e: Exception) {
                mutableBasketState.postValue(Error(e))
            }
        }
    }

    fun setPayVariant(variant: PaymentVariant) {
        when (variant) {
            is CardVariant -> mutablePayVariantState.postValue(CardVariant)
            is CashVariant -> mutablePayVariantState.postValue(CashVariant)
            is GPayVariant -> mutablePayVariantState.postValue(GPayVariant)
        }
    }

    fun setComment(comment: String) {
        _commentTextLivedata.postValue(comment)
    }

    //общий метод для POST-запроса на отправку заказа после оплаты(если картой или GPay)/выбора налички
    fun onSendOrder() {
    }

    private fun Int.inCoins() =
        (this * 100).toLong()

    private fun Double.inCoins() =
        (this * 100).toLong()
}