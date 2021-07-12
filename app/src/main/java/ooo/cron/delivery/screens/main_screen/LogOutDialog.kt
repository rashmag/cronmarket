package ooo.cron.delivery.screens.main_screen

import ooo.cron.delivery.screens.AcceptDialog

/**
 * Created by Ramazan Gadzhikadiev on 09.06.2021.
 */
class LogOutDialog(
    onAccept: () -> Unit
) : AcceptDialog(
    {},
    onAccept
) {
    override val title: String = "Вы действительно хотите выйти?"
    override val message: String = ""
}