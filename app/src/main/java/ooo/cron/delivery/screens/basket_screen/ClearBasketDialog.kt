package ooo.cron.delivery.screens.basket_screen

import ooo.cron.delivery.screens.AcceptDialog

/**
 * Created by Ramazan Gadzhikadiev on 02.06.2021.
 */
class ClearBasketDialog(
    onAccept: () -> Unit
) : AcceptDialog({}, onAccept) {
    override val title = "Очистить корзину"
    override val message = ""
}