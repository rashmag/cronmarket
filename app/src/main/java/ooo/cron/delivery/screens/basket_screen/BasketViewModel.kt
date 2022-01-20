package ooo.cron.delivery.screens.basket_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.BasketInteractor
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketPersonsReq
import ooo.cron.delivery.data.network.models.RemoveBasketItemReq
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.BaseViewModel
import ooo.cron.delivery.utils.SingleLiveEvent
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
            ErrorHandler(
                action = {
                    val basket = interactor.getBasket(interactor.getBasketId())
                    basket.onSuccess {
                        mutableBasket.postValue(basket.getOrNull())
                    }
                },
                onConnectionError = { connectionErrorScreen.call()},
                onAnyError = { anyErrorScreen.call()})
        }
    }

    fun onPlusClicked(dish: BasketDish, extraQuantity: Int) {
        viewModelScope.launch(handler) {
            val editor = prepareEditor(dish, extraQuantity)
            ErrorHandler(
                action = {
                   val basket = editor?.let { interactor.increaseProductInBasket(it)
                   }
                    mutableBasket.postValue(basket!!)
                },
                onConnectionError = { connectionErrorScreen.call() },
                onAnyError = { anyErrorScreen.call() }
            )
        }
    }

    fun onMinusClicked(dish: BasketDish, extraQuantity: Int) {
        viewModelScope.launch(handler) {
            val editor = prepareEditor(dish, extraQuantity)
            ErrorHandler(
                action = {
                    val basket = editor?.let {
                        interactor.decreaseProductInBasket(it)
                    }
                    mutableBasket.postValue(basket!!)
                },
                onConnectionError = { connectionErrorScreen.call() },
                onAnyError = { anyErrorScreen.call() }
            )
        }
    }

    fun onPersonsQuantityEdited(quantity: Int) {
        viewModelScope.launch(handler) {
            ErrorHandler(
                action = {
                    val basket = interactor.editPersonsQuantity(
                        BasketPersonsReq(
                            mutableBasket.value!!.id,
                            quantity
                        )
                    )
                    mutableBasket.postValue(basket)
                },
                onConnectionError = { connectionErrorScreen.call() },
                onAnyError = { anyErrorScreen.call() }
            )
        }
    }

    fun onClearBasketAccepted() {
        viewModelScope.launch(handler) {
            ErrorHandler(
                action =  {
                    val basket = interactor.clearBasket(
                        BasketClearReq(
                            mutableBasket.value!!.id
                        )
                    )
                    mutableBasket.value = basket //?
                    basketClearAccept.call()
                },
                onConnectionError = { connectionErrorScreen.call() },
                onAnyError = { anyErrorScreen.call() }
            )
        }
    }

    fun onItemRemoveClicked(product: BasketDish?) {
        viewModelScope.launch(handler) {
            ErrorHandler(
                action = {
                    val basket = interactor.removeBasketItem(
                        RemoveBasketItemReq(
                            product?.id.orEmpty(),
                            mutableBasket.value !!.id
                        )
                    )
                    mutableBasket.postValue(basket)
                },
                onConnectionError = { connectionErrorScreen.call() },
                onAnyError = { anyErrorScreen.call() }
            )
        }
    }

    fun onMakeOrderClicked() {
        if (interactor.getToken().refreshToken.isEmpty())
            navigationAuth.call()
        mutableBasket.value?.let {
            interactor.writeBasket(it) }
        showingMakeOrderDialog.call()

    }

    private fun prepareEditor(dish: BasketDish, extraQuantity: Int): BasketEditorReq? {
        val addingProduct = dish.copy(
            quantity = extraQuantity
        )
        val basketEditor = mutableBasket.value
        val editor = basketEditor?.let {
            BasketEditorReq(
                it.id,
                it.partnerId,
                it.marketCategoryId,
                Gson().toJson(addingProduct)
            )
        }
        return editor
    }
}