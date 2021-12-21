package ooo.cron.delivery.screens.pay_dialog_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.item_main_market_category.view.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.Basket.Companion.deserializeDishes
import ooo.cron.delivery.databinding.DialogOrderBinding
import ru.tinkoff.acquiring.sdk.AcquiringSdk
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
import ru.tinkoff.acquiring.sdk.localization.AsdkSource
import ru.tinkoff.acquiring.sdk.localization.Language
import ru.tinkoff.acquiring.sdk.models.DarkThemeMode
import ru.tinkoff.acquiring.sdk.models.Item
import ru.tinkoff.acquiring.sdk.models.Receipt
import ru.tinkoff.acquiring.sdk.models.enums.CheckType
import ru.tinkoff.acquiring.sdk.models.enums.Tax
import ru.tinkoff.acquiring.sdk.models.enums.Taxation
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions
import ru.tinkoff.acquiring.sdk.utils.Money
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderBottomDialog() : BottomSheetDialogFragment() {

    //var basket: Basket? = null
    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    private lateinit var payTinkoff: PayTinkoff

    @Inject
    lateinit var binding: DialogOrderBinding

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        payTinkoff = PayTinkoff(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onCreateView()

        AcquiringSdk.isDeveloperMode = true

        viewModel.basket.observe(viewLifecycleOwner, {
            basket = it
        })
        viewModel.phone.observe(viewLifecycleOwner, {
            phone = it
        })
        viewModel.basketState.observe(viewLifecycleOwner, {
            when (it) {
                is Loading -> showLoadingState()
                is Default -> removeLoadingState()
                is Error -> showError()
            }
        })

        binding.tvBasketAmount.text = requireContext().getString(
            R.string.price, ((basket?.amount?.toInt() ?: 0) + (basket?.deliveryCost?.toInt()
                ?: 0)).toString()
        )

        viewModel.commentTextLiveData.observe(viewLifecycleOwner) {
            with(binding.etComments) {
                text = if(it.isNotBlank()) it else getString(R.string.order_comment)
                val bg =
                    if (it.isNotBlank()) R.drawable.bg_true_light else R.drawable.bg_main_address_correct
                setBackgroundResource(bg)
                gravity = if (it.isNotBlank()) Gravity.START else Gravity.CENTER
                val endIcon = if(it.isNotBlank())R.drawable.ic_market_category_tag_check else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, endIcon, 0)
            }
        }

        binding.etComments.showSoftInputOnFocus = false
        binding.etComments.setOnClickListener {
            OrderCommentBottomDialog().show(parentFragmentManager, "")
        }

        binding.bankCardPayment.setOnClickListener {
            paymentVariant = CardVariant
            binding.bankCardPayment.setBackgroundResource(R.drawable.bg_btn_payment_selected)
            binding.cashPayment.setBackgroundResource(R.drawable.bg_btn_payment_not_selected)
            binding.gpayPayment.setBackgroundResource(R.drawable.bg_btn_payment_not_selected)
            binding.btnOrder.setBackgroundResource(R.drawable.bg_btn_order)
            binding.btnOrder.text = getString(R.string.order_cash_payment)
        }

        binding.cashPayment.setOnClickListener {
            paymentVariant = CashVariant
            binding.bankCardPayment.setBackgroundResource(R.drawable.bg_btn_payment_not_selected)
            binding.cashPayment.setBackgroundResource(R.drawable.bg_btn_payment_selected)
            binding.gpayPayment.setBackgroundResource(R.drawable.bg_btn_payment_not_selected)
            binding.btnOrder.setBackgroundResource(R.drawable.bg_btn_order)
        }

        binding.gpayPayment.setOnClickListener {
            paymentVariant = GPayVariant
            binding.bankCardPayment.setBackgroundResource(R.drawable.bg_btn_payment_not_selected)
            binding.cashPayment.setBackgroundResource(R.drawable.bg_btn_payment_not_selected)
            binding.gpayPayment.setBackgroundResource(R.drawable.bg_btn_payment_selected)
            binding.btnOrder.setBackgroundResource(R.drawable.bg_btn_payment_gpay_selected)
            binding.btnOrder.setBackgroundDrawable(resources.getDrawable(R.drawable.gpay_button_content_black))
        }

        binding.btnOrder.setOnClickListener {
            when (paymentVariant) {
                is CardVariant -> payWithCard()
                else -> showNoPaymentDialog()
            }
        }
    }

    private fun showError() {
        TODO("Not yet implemented")
    }

    private fun removeLoadingState() {
        binding.vgProgress.root.isVisible = !isVisible
    }

    private fun showLoadingState() {
        binding.vgProgress.root.isVisible = !isVisible
    }

    private fun showNoPaymentDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Необходимо выбрать способ оплаты")
            .setPositiveButton("Ок") { dialog, id ->
                dialog.cancel()
            }
        builder.show()
    }
}