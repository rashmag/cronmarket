package ooo.cron.delivery.screens.pay_dialog_screen

import ooo.cron.delivery.data.network.models.Basket
import java.lang.Exception

sealed class BasketState

object Loading: BasketState()
class Error(e: Exception): BasketState()
class Default(basket: Basket): BasketState()