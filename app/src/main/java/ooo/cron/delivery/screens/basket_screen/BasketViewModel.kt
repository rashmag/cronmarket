package ooo.cron.delivery.screens.basket_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.BasketInteractor
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.BaseViewModel
import ooo.cron.delivery.utils.Error
import ooo.cron.delivery.utils.NoConnection
import ooo.cron.delivery.utils.Result
import ooo.cron.delivery.utils.SingleLiveEvent
import ooo.cron.delivery.utils.Success
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.01.2022
 * */

class BasketViewModel @Inject constructor(
    private val interactor: BasketInteractor
) : BaseViewModel() {
    private val handler = CoroutineExceptionHandler { context, exception ->
        Log.e("exception", exception.toString())
    }
    private val mutableBasket: MutableLiveData<Basket> = MutableLiveData()
    val basket: LiveData<Basket> get() = mutableBasket
    val basketClearAccept: SingleLiveEvent<Unit> = SingleLiveEvent()
    val navigationAuth: SingleLiveEvent<Unit> = SingleLiveEvent()
    val showingMakeOrderDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    override val connectionErrorScreen: SingleLiveEvent<Unit> = SingleLiveEvent()
    override val anyErrorScreen: SingleLiveEvent<Unit> = SingleLiveEvent()


    fun onStart() {
        viewModelScope.launch(handler) {
            interactor.getBasket(interactor.getBasketId()).process { mutableBasket.postValue(it) }
        }
    }

    fun onPlusClicked(dish: BasketDish, extraQuantity: Int) {
        viewModelScope.launch(handler) {
            val editor = prepareEditor(dish, extraQuantity)
            editor?.let { interactor.increaseProductInBasket(it) }
                ?.process { mutableBasket.postValue(it) }
        }
    }

    fun onMinusClicked(dish: BasketDish, extraQuantity: Int) {
        viewModelScope.launch(handler) {
            val editor = prepareEditor(dish, extraQuantity)
            editor?.let { interactor.decreaseProductInBasket(it) }
                ?.process { mutableBasket.postValue(it) }
        }
    }

    fun onPersonsQuantityEdited(quantity: Int) {
        viewModelScope.launch(handler) {
            val basketPersonsReq = mutableBasket.value?.let {
                interactor.getBasketPersonReq(it,quantity)
            }
            basketPersonsReq?.let { interactor.editPersonsQuantity(it) }
                ?.process { mutableBasket.postValue(it) }
        }
    }

    fun onClearBasketAccepted() {
        viewModelScope.launch(handler) {
            val basketClearReq = mutableBasket.value?.let { interactor.getBasketClearReq(it) }
            basketClearReq?.let { interactor.clearBasket(it) }
                ?.process { mutableBasket.postValue(it) }
            basketClearAccept.call()
        }
    }

    fun onItemRemoveClicked(product: BasketDish?) {
        viewModelScope.launch(handler) {
            val removeBasketItemReq = mutableBasket.value?.let {
                interactor.getRemoveBasketItemReq(product, it)
            }
            removeBasketItemReq?.let {
                interactor.removeBasketItem(it)
            }?.process { mutableBasket.postValue(it) }

        }
    }

    fun onMakeOrderClicked() {
        if (interactor.getToken().refreshToken.isEmpty())
            navigationAuth.call()
        mutableBasket.value?.let {
            interactor.writeBasket(it)
        }
        showingMakeOrderDialog.call()

    }

    private fun prepareEditor(dish: BasketDish, extraQuantity: Int): BasketEditorReq? {
        val addingProduct = dish.copy(
            quantity = extraQuantity
        )
        val basket = mutableBasket.value
        return basket?.let {
            interactor.getBasketEditorReq(it, addingProduct)
        }
    }

    //экстеншн
    private fun <T> Result<T>.process(onSuccess: (T) -> Unit) {
        return when (this) {
            is Success -> onSuccess.invoke(body)
            is Error -> anyErrorScreen.call()
            is NoConnection -> connectionErrorScreen.call()
        }
    }
}