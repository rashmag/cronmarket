package ooo.cron.delivery.screens.pay_dialog_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import ooo.cron.delivery.App
import ooo.cron.delivery.BuildConfig
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.databinding.DialogOrderBinding
import ru.tinkoff.acquiring.sdk.AcquiringSdk
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring.Companion.RESULT_ERROR
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderBottomDialog : BottomSheetDialogFragment() {

    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    private lateinit var payTinkoff: PayTinkoff
    private var payCallback : PayClickCallback? = null

    @Inject
    lateinit var binding: DialogOrderBinding

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    private var isDeliveryInKhas: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        context.let { context ->
            if (context is PayClickCallback) payCallback = context
        }
        payTinkoff = PayTinkoff(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    //TODO аншелвить изменения и показать результат в экране
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.d("result_codeD", "Done$resultCode")
                val error = data?.getSerializableExtra(TinkoffAcquiring.EXTRA_ERROR)
                if (error != null) {
                    viewModel.onPaymentSuccess()
                } else viewModel.onPaymentFailed()

            }
            Activity.RESULT_CANCELED -> Toast.makeText(requireContext(), "Оплата отменена", Toast.LENGTH_SHORT).show()
            RESULT_ERROR -> {
                viewModel.onPaymentFailed()
                Log.d("result_codeD", "Fail$resultCode")
                Log.e("t_error_code",(data?.getSerializableExtra(TinkoffAcquiring.EXTRA_ERROR) as Throwable).toString())
            }
        else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AcquiringSdk.isDeveloperMode = BuildConfig.DEBUG
        AcquiringSdk.isDebug = BuildConfig.DEBUG
        viewModel.onViewCreated()
        viewModel.callingDeliveryCostInfo.observe(viewLifecycleOwner, {
            isDeliveryInKhas = it
        })
        viewModel.basketState.observe(viewLifecycleOwner, { basketState ->
            when (basketState) {
                is Loading -> showLoadingState()
                is Default -> removeLoadingState(basketState.basket)
                is Error -> showError()
            }
        })
        viewModel.paymentStatus.observe(viewLifecycleOwner, {
            openOrderStatusFragment(it)
        })
        viewModel.callingPayInfoDialog.observe(viewLifecycleOwner, {
            showInformDialog(R.string.order_no_payment_inform_message)
        })
        viewModel.payVariantState.observe(viewLifecycleOwner, {
            binding.btnOrder.text = getString(R.string.order_cash_payment)
            binding.btnOrder.setBackgroundResource(R.drawable.bg_btn_order)
            binding.btnGpayOrder.isVisible = it is GPayVariant
            binding.btnOrder.isVisible = it !is GPayVariant
        })
        viewModel.commentTextLiveData.observe(viewLifecycleOwner) {
            updateCommentText(it)
        }
        viewModel.payData.observe(viewLifecycleOwner, {
            payTinkoff.payWithCard(it.amountSum, it.receipt, it.phone)
        })
        binding.etComments.showSoftInputOnFocus = false
        binding.etComments.setOnClickListener {
            OrderCommentBottomDialog().show(parentFragmentManager, "")
        }
        binding.rgPayment.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.card_radiobutton -> viewModel.setPayVariant(CardVariant)
                R.id.cash_radiobutton -> viewModel.setPayVariant(CashVariant)
                R.id.gpay_radiobutton -> viewModel.setPayVariant(GPayVariant)
            }
        }
        binding.btnOrder.setOnClickListener {
           viewModel.onPayClicked()
        }

        binding.btnGpayOrder.setOnClickListener {
            viewModel.onPayClicked()
        }
    }

    private fun updateCommentText(comment: String) {
        with(binding.etComments) {
            text = if (comment.isNotBlank()) comment else getString(R.string.order_comment)
            val bg =
                if (comment.isNotBlank()) R.drawable.bg_true_light else R.drawable.bg_main_address_correct
            setBackgroundResource(bg)
            gravity = if (comment.isNotBlank()) Gravity.START else Gravity.CENTER
            val endIcon = if (comment.isNotBlank()) R.drawable.ic_market_category_tag_check else 0
            setCompoundDrawablesWithIntrinsicBounds(0, 0, endIcon, 0)
        }
    }

    //Открытие полноэкранного статуса заказа в экране корзины
    private fun openOrderStatusFragment(isSuccessPayment: Boolean) {
        /*TODO сделать переход на фрагмент с открытием полноэкранного статуса заказа
        *  isSuccessPayment передавать в экран через Bundle или Extra*/
        dismiss()
        payCallback?.onPayClicked(isSuccessPayment)
    }

    private fun injectDependencies() {
        App.appComponent.orderComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }

    private fun showError() {
        //TODO Показ ошибки получения баскета
    }

    private fun removeLoadingState(basket: Basket) {
        binding.vgProgress.root.isVisible = !isVisible
        if (isDeliveryInKhas == true) {
            showInformDialog(R.string.order_inform_delivery_cost_message)
            binding.tvBasketAmount.text = requireContext().getString(
                R.string.price, basket.amount.toInt().toString()
            )
        } else
        binding.tvBasketAmount.text = requireContext().getString(
            R.string.price, (basket.amount.toInt() + 99).toString()
        )
        binding.orderAmount.visibility = View.VISIBLE
    }

    private fun showLoadingState() {
        binding.vgProgress.root.isVisible = !isVisible
    }

    private fun showInformDialog(resMessage:Int) {
        val dialog = MaterialDialog(requireContext())
            .customView(R.layout.dialog_inform)
        val customView = dialog.getCustomView()
        val button = customView.rootView.findViewById<MaterialButton>(R.id.btn_inform_accept)
        button.text = getString(R.string.order_inform_attention_btn)
        button.setOnClickListener {
            dialog.cancel()
        }
        val title = customView.rootView.findViewById<AppCompatTextView>(R.id.tv_inform_title)
        title.text = getString(R.string.order_inform_attention)
        val message = customView.rootView.findViewById<AppCompatTextView>(R.id.tv_inform_message)
        message.text = getString(resMessage)
        dialog.show()
    }
}