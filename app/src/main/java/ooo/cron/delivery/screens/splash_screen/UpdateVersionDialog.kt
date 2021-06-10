package ooo.cron.delivery.screens.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.AcceptDialog

/**
 * Created by Ramazan Gadzhikadiev on 10.06.2021.
 */
class UpdateVersionDialog(
    onCancel: () -> Unit,
    onAccept: () -> Unit
) : AcceptDialog(
    onCancel,
    onAccept
) {
    override val title =
        "У нас появилось много\nнового и интересного!\nПожалуйста, обновите приложение."
    override val message = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            binding.btnAccept.backgroundTintList =
                ContextCompat.getColorStateList(context!!, R.color.errors_true)
            binding.btnAccept.text = "Обновить"

            binding.ivAcceptWarning.setImageResource(R.drawable.ic_update)
        }
    }
}