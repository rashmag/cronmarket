package ooo.cron.delivery.screens.partners_screen

import ooo.cron.delivery.screens.base.AcceptDialog

/**
 * Created by Ramazan Gadzhikadiev on 01.06.2021.
 */
class ClearBasketDialog(
    onCancel: () -> Unit,
    onAccept: () -> Unit
) : AcceptDialog(
    onCancel,
    onAccept
) {
    override val title = "Очистить корзину?"
    override val message =
        "Нельзя заказать товары из разных заведений. Подтверждая вы удалите все товары из корзины."
}