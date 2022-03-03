package ooo.cron.delivery.screens.partners_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.screens.base.AcceptDialog

/**
 * Created by Ramazan Gadzhikadiev on 01.06.2021.
 */
class RequiredAddressDialog(
    onCancel: () -> Unit,
    onAccept: () -> Unit
) : AcceptDialog(
    onCancel,
    onAccept
) {
    override val title = "Укажите адрес доставки"
    override val message = "Для того, чтобы добавить позицию в корзину, укажите адрес доставки"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            binding.ivAcceptWarning.visibility = View.INVISIBLE
        }
    }
}