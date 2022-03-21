package ooo.cron.delivery.screens.basket_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.data.BasketInteractor
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.BaseViewModel
import ooo.cron.delivery.utils.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.01.2022
 * */

class BasketViewModel @Inject constructor(
    private val interactor: BasketInteractor,
    private val analytics: BaseAnalytics
) : BaseViewModel() {
    private val handler = CoroutineExceptionHandler { context, exception ->
        Log.e("exception", exception.toString())
    }
    private val mutableBasket: MutableLiveData<Pair<Basket, List<BasketDish>>> = MutableLiveData()
    val basket: LiveData<Pair<Basket, List<BasketDish>>> get() = mutableBasket
    val basketClearAccept: SingleLiveEvent<Unit> = SingleLiveEvent()
    val navigationAuth: SingleLiveEvent<Unit> = SingleLiveEvent()
    val showingMakeOrderDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    val showingOrderFromDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    val marketCategoryId: SingleLiveEvent<Int> = SingleLiveEvent()

    fun onStart() {
        viewModelScope.launch(handler) {
            val basket = interactor.getBasketId()?.let { interactor.getBasket(it) }
            basket?.process { mutableBasket.postValue(Pair(it, deserializeDishes(it))) }
            analytics.trackOpenBasketScreen(
                quantity = basket?.process { deserializeDishes(it) } as Int,
                phoneNumber = interactor.getUserPhone().toString(),
                amount = basket.process { it } as Int
            )
        }
    }

    fun onPlusClicked(dish: BasketDish, extraQuantity: Int) {
        viewModelScope.launch(handler) {
            val editor = prepareEditor(dish, extraQuantity)
            editor?.let { interactor.increaseProductInBasket(it) }
                ?.process { mutableBasket.postValue(Pair(it, deserializeDishes(it))) }
        }
    }

    fun onMinusClicked(dish: BasketDish, extraQuantity: Int) {
        viewModelScope.launch(handler) {
            val editor = prepareEditor(dish, extraQuantity)
            editor?.let { interactor.decreaseProductInBasket(it) }
                ?.process { mutableBasket.postValue(Pair(it, deserializeDishes(it))) }
        }
    }

    fun onPersonsQuantityEdited(quantity: Int) {
        viewModelScope.launch(handler) {
            val basketPersonsReq = mutableBasket.value?.let {
                interactor.getBasketPersonReq(it.first, quantity)
            }
            basketPersonsReq?.let { interactor.editPersonsQuantity(it) }
                ?.process { mutableBasket.postValue(Pair(it, deserializeDishes(it))) }
        }
    }

    fun onClearBasketAccepted() {
        viewModelScope.launch(handler) {
            val basketClearReq = mutableBasket.value?.let { interactor.getBasketClearReq(it.first) }
            basketClearReq?.let { interactor.clearBasket(it) }
                ?.process { mutableBasket.postValue(Pair(it, deserializeDishes(it))) }
            basketClearAccept.call()
        }
    }

    fun onItemRemoveClicked(product: BasketDish?) {
        viewModelScope.launch(handler) {
            val removeBasketItemReq = mutableBasket.value?.let {
                interactor.getRemoveBasketItemReq(product, it.first)
            }
            removeBasketItemReq?.let {
                interactor.removeBasketItem(it)
            }?.process { mutableBasket.postValue(Pair(it, deserializeDishes(it))) }
        }
    }

    fun onMakeOrderClicked(orderAmount: Int) {
        if (interactor.getToken()?.refreshToken == null)
            navigationAuth.call()

        viewModelScope.launch(handler) {
            val basket = interactor.getBasketId()?.let { interactor.getBasket(it) }
            if (basket?.process { (it.amount ?: 0.0) < orderAmount } as Boolean) {
                showingOrderFromDialog.call()
            }
        }
        mutableBasket.value?.let {
            interactor.writeBasket(it.first)
        }
        showingMakeOrderDialog.call()
    }

    fun onAdapterInited() {
        viewModelScope.launch {
            interactor.getBasketId()?.let {
                interactor.getBasket(it)
            }?.process { marketCategoryId.postValue(it.marketCategoryId) }
        }
    }

    private fun prepareEditor(dish: BasketDish, extraQuantity: Int): BasketEditorReq? {
        val addingProduct = dish.copy(
            quantity = extraQuantity
        )
        val basket = mutableBasket.value
        return basket?.let {
            interactor.getBasketEditorReq(it.first, addingProduct)
        }
    }

    private fun deserializeDishes(basket: Basket?) =
        Gson().fromJson(basket?.content, Array<BasketDish>::class.java)
            .asList()
}