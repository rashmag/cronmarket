package ooo.cron.delivery.screens

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ooo.cron.delivery.databinding.DialogAcceptBinding

/**
 * Created by Ramazan Gadzhikadiev on 31.05.2021.
 */
class AcceptDialog(val onCancel: () -> Unit, val onAccept: () -> Unit) : DialogFragment() {

    private lateinit var binding: DialogAcceptBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAcceptBinding.inflate(inflater)
        binding.btnAccept.setOnClickListener {
            onAccept()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            onCancel()
            dismiss()
        }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}