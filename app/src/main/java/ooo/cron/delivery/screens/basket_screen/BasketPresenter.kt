package ooo.cron.delivery.screens.basket_screen

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.utils.extensions.orZero

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class BasketPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
    private var basket: Basket?,
    private val analytics: BaseAnalytics
) :
    BaseMvpPresenter<BasketContract.View>(),
    BasketContract.Presenter {

    override fun onStartView() {
        mainScope.launch {
            withErrorsHandle(
                action = {
                    val response = dataManager.getBasket(dataManager.readUserBasketId())
                    if (response.isSuccessful) {
                        basket = response.body()!!
                    }
                    analytics.trackOpenBasketScreen(
                        quantity = deserializeDishes().size,
                        phoneNumber = dataManager.readUserPhone().toString(),
                        amount = basket?.amount?.toInt().orZero()
                    )
                },
                onConnectionError = { view?.showConnectionErrorScreen() },
                onAnyError = { view?.showAnyErrorScreen() }
            )

            view?.updateBasket(deserializeDishes(), basket!!.cutleryCount)
            val formatter = DecimalFormat("#.##").apply {
                roundingMode = RoundingMode.CEILING
            }

            view?.updateBasketAmount(formatter.format(basket!!.amount))
        }
    }

    override fun plusClick(dish: BasketDish, extraQuantity: Int) {
        mainScope.launch {
            val addingProduct = dish.copy(
                quantity = extraQuantity
            )

            val editor = BasketEditorReq(
                basket!!.id,
                basket!!.partnerId,
                basket!!.marketCategoryId,
                Gson().toJson(addingProduct)
            )

            withErrorsHandle(
                {
                    basket = dataManager.increaseProductInBasket(editor)
                    view?.updateBasket(deserializeDishes(), basket!!.cutleryCount)
                    val formatter = DecimalFormat("#.##").apply {
                        roundingMode = RoundingMode.CEILING
                    }

                    view?.updateBasketAmount(formatter.format(basket!!.amount))
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

    override fun minusClick(dish: BasketDish, unwantedQuantity: Int) {
        mainScope.launch {
            val addingProduct = dish.copy(
                quantity = unwantedQuantity
            )

            val editor = BasketEditorReq(
                basket!!.id,
                basket!!.partnerId,
                basket!!.marketCategoryId,
                Gson().toJson(addingProduct)
            )

            withErrorsHandle(
                {
                    basket = dataManager.decreaseProductInBasket(editor)
                    view?.updateBasket(deserializeDishes(), basket!!.cutleryCount)
                    val formatter = DecimalFormat("#.##").apply {
                        roundingMode = RoundingMode.CEILING
                    }

                    view?.updateBasketAmount(formatter.format(basket!!.amount))
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

    override fun personsQuantityEdited(quantity: Int) {
        mainScope.launch {
            withErrorsHandle(
                {
                    basket = dataManager.editPersonsQuantity(
                        BasketPersonsReq(
                            basket?.id.orEmpty(),
                            quantity
                        )
                    )

                    view?.updateBasket(
                        deserializeDishes(),
                        basket?.cutleryCount ?: 0
                    )

                    val formatter = DecimalFormat("#.##").apply {
                        roundingMode = RoundingMode.CEILING
                    }

                    view?.updateBasketAmount(formatter.format(basket?.amount))
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun removeItemClicked(product: BasketDish?) {
        mainScope.launch {
            basket = dataManager.removeBasketItem(
                RemoveBasketItemReq(
                    product?.id.orEmpty(),
                    basket!!.id
                )
            )
            view?.updateBasket(deserializeDishes(), basket!!.cutleryCount)
        }
    }

    override fun clearClicked() {
        view?.showClearBasketDialog()
    }

    override fun clearBasketAccepted() {
        mainScope.launch {
            withErrorsHandle(
                {
                    basket = dataManager.clearBasket(
                        BasketClearReq(basket!!.id)
                    )
                    view?.close()
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun getMarketCategoryId(): Int {
        return basket?.marketCategoryId ?: -1
    }

    override fun clickMakeOrder() {
        if (dataManager.readToken()?.refreshToken?.isEmpty() == true) {
            view?.navigateAuthorization()
            return
        }

        if((basket?.amount ?: EMPTY_BASKET) < view?.getMinOrderAmount().orZero()){
            view?.showOrderFromDialog()
            return
        }

        dataManager.writeBasket(basket!!)
        //view?.navigateMakeOrderScreen(basket!!)
    }

    private fun deserializeDishes() =
        Gson().fromJson(basket!!.content, Array<BasketDish>::class.java)
            .asList()

    private companion object{
        const val EMPTY_BASKET = 0.0
    }
}