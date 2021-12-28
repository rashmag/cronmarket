package ooo.cron.delivery.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ooo.cron.delivery.databinding.DialogInformBinding

abstract class InformDialog() : DialogFragment() {

    protected lateinit var binding: DialogInformBinding

    protected abstract val title: String
    protected abstract val message: String
    protected abstract val btn_title: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogInformBinding.inflate(inflater)
        binding.tvInformTitle.text = title
        binding.tvInformMessage.text = message
        binding.btnInformAccept.text = btn_title
        binding.btnInformAccept.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}