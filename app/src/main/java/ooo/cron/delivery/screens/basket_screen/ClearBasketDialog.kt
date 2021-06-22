package ooo.cron.delivery.screens.basket_screen

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ooo.cron.delivery.databinding.DialogAcceptBinding
import ooo.cron.delivery.databinding.DialogClearBasketBinding

/**
 * Created by Ramazan Gadzhikadiev on 02.06.2021.
 */
class ClearBasketDialog(
    val onAccept: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogClearBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogClearBasketBinding.inflate(inflater)
        binding.tvAccept.setOnClickListener {
            onAccept()
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}