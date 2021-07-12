package ooo.cron.delivery.screens.basket_screen

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketPersonsReq
import ooo.cron.delivery.data.network.models.RemoveBasketItemReq
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class BasketPresenter @Inject constructor(
    val dataManager: DataManager,
    val mainScope: CoroutineScope
) :
    BaseMvpPresenter<BasketContract.View>(),
    BasketContract.Presenter {

    private var basket: Basket? = null

    override fun onStartView() {
        mainScope.launch {
            withErrorsHandle(
                {
                    val response = dataManager.getBasket(dataManager.readUserBasket())
                    if (response.isSuccessful) {
                        basket = response.body()!!
                    }
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
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
            basket = dataManager.editPersonsQuantity(
                BasketPersonsReq(
                    basket!!.id,
                    quantity
                )
            )
            view?.updateBasket(
                deserializeDishes(),
                basket!!.cutleryCount
            )
            val formatter = DecimalFormat("#.##").apply {
                roundingMode = RoundingMode.CEILING
            }

            view?.updateBasketAmount(formatter.format(basket!!.amount))
        }
    }

    override fun removeItemClicked(product: BasketDish) {
        mainScope.launch {
            basket = dataManager.removeBasketItem(
                RemoveBasketItemReq(
                    product.id,
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

    override fun clickMakeOrder() {
        if (dataManager.readToken().refreshToken.isEmpty()) {
            view?.navigateAuthorization()
            return
        }

        view?.navigateMakeOrderScreen(basket?.amount ?: 0.0)
    }

    private fun deserializeDishes() =
        Gson().fromJson(basket!!.content, Array<BasketDish>::class.java)
            .asList()
}