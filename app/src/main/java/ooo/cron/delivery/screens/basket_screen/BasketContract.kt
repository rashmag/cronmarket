package ooo.cron.delivery.screens.basket_screen

import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
interface BasketContract {
    interface View : MvpView {
        fun updateBasket(basket: List<BasketDish>, partnersQuantity: Int)

        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()

        fun showClearBasketDialog()
        fun navigateMakeOrderScreen(amount: Double)

        fun updateBasketAmount(price: String)

        fun close()
    }

    interface Presenter : MvpPresenter<View> {
        fun onStartView()
        fun plusClick(dish: BasketDish, extraQuantity: Int)
        fun minusClick(dish: BasketDish, unwantedQuantity: Int)
        fun personsQuantityEdited(quantity: Int)
        fun removeItemClicked(product: BasketDish)
        fun clickMakeOrder()
        fun clearClicked()
        fun clearBasketAccepted()
    }
}