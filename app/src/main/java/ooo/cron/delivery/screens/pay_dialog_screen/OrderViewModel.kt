package ooo.cron.delivery.screens.pay_dialog_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.OrderPrefsRepository
import ooo.cron.delivery.data.OrderRestRepository
import ooo.cron.delivery.data.network.models.Basket
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderViewModel @Inject constructor(
    private val restRepo: OrderRestRepository,
    private val prefsRepo: OrderPrefsRepository
) : ViewModel() {

    private val mutableBasket: MutableLiveData<Basket> = MutableLiveData()
    val basket: LiveData<Basket> get() = mutableBasket
    private val mutablePhone: MutableLiveData<String> = MutableLiveData()
    val phone: LiveData<String> get() = mutablePhone
    private val mutableBasketState: MutableLiveData<BasketState> = MutableLiveData()
    val basketState: LiveData<BasketState> get() = mutableBasketState

    init {
        Log.e("app", "inited")
    }
    private val _commentTextLivedata = MutableLiveData("")
    val commentTextLiveData: LiveData<String> = _commentTextLivedata

    fun setComment(comment: String){
        _commentTextLivedata.postValue(comment)

    }

    fun onCreateView() = viewModelScope.launch {
        val phone = prefsRepo.readUserPhone()
        loadBasket()
        mutablePhone.postValue(phone.toString())
    }

    private fun loadBasket() {
        mutableBasketState.postValue(Loading)
        val id = prefsRepo.readUserBasketId()
        viewModelScope.launch {
            try {
                val basket = restRepo.getBasket(id)
                Log.d("basket", basket.toString())
                mutableBasketState.postValue(Default(basket))
            } catch (e: Exception) {
                mutableBasketState.postValue(Error(e))
            }
        }
    }

    //общий метод для POST-запроса на отправку заказа после оплаты(если картой или GPay)/выбора налички
    fun onSendOrder() {
    }
}