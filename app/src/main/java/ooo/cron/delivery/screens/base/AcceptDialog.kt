package ooo.cron.delivery.screens.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ooo.cron.delivery.databinding.DialogAcceptBinding

/**
 * Created by Ramazan Gadzhikadiev on 31.05.2021.
 */
abstract class AcceptDialog(val onCancel: () -> Unit, val onAccept: () -> Unit) : DialogFragment() {

    protected lateinit var binding: DialogAcceptBinding

    protected abstract val title: String
    protected abstract val message: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAcceptBinding.inflate(inflater)
        binding.tvAcceptTitle.text = title
        binding.tvAcceptMessage.text = message
        onCancel()
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
}