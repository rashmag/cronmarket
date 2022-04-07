package ooo.cron.delivery.screens.payment_status_screen
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.FragmentPaymentStatusBinding
import ooo.cron.delivery.screens.main_screen.MainActivity
import javax.inject.Inject

class PaymentStatusFragment(): Fragment() {

    companion object {
        const val PAYMENT_FLAG = "payment_flag"

        fun newInstance(isSuccessPayment: Boolean): PaymentStatusFragment {
            val bundle = Bundle().apply {
                putBoolean(PAYMENT_FLAG, isSuccessPayment)
            }
            return PaymentStatusFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var binding: FragmentPaymentStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appComponent.paymentStatusComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getBoolean(PAYMENT_FLAG) == true) {
            binding.tvOrderStatus.isSelected = true
            binding.ivPaymentStatus.isSelected = true
            binding.tvOrderStatus.text = getString(R.string.ordering_pay_status_done)
            binding.tvOrderDetailsStatus.text = getString(R.string.ordering_pay_status_done_details_title)
            binding.btnRepeatOrder.text = getString(R.string.order_pay_success_title)
        } else {
            binding.tvOrderStatus.text = getString(R.string.ordering_pay_status_error)
            binding.tvOrderStatus.isSelected = false
            binding.ivPaymentStatus.isSelected = false
            binding.tvOrderDetailsStatus.text = getString(R.string.ordering_pay_status_error_detail_title)
            binding.btnRepeatOrder.text = getString(R.string.order_pay_fail_title)
        }
        binding.btnRepeatOrder.setOnClickListener {
            if (arguments?.getBoolean(PAYMENT_FLAG) == true) {
                openMainScreen()
            } else parentFragmentManager.popBackStack()
        }

    }

    private fun openMainScreen() {
        activity?.finish()
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

}