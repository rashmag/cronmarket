package ooo.cron.delivery.screens.partners_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.OrderFromDialogBinding

class OrderFromDialog(
    private val price: Int
) : DialogFragment() {

    private var _binding: OrderFromDialogBinding ?= null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = OrderFromDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            tvTitle.text = getString(
                R.string.partners_activity_dialog_min_price_title,
                price.toString()
            )

            btnAccept.setOnClickListener {
                dismiss()
            }
        }
    }
}