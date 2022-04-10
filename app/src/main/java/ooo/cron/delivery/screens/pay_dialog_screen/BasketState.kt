package ooo.cron.delivery.screens.pay_dialog_screen

import ooo.cron.delivery.data.network.models.Basket

/**
 * Created by Maya Nasrueva on 17.12.2021
 * */

sealed class BasketState

object Loading: BasketState()
object Error: BasketState()
class Default(val basket: Basket): BasketState()