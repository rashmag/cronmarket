package ooo.cron.delivery.screens.pay_dialog_screen

import android.content.Context
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ooo.cron.delivery.R
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
import ru.tinkoff.acquiring.sdk.localization.AsdkSource
import ru.tinkoff.acquiring.sdk.localization.Language
import ru.tinkoff.acquiring.sdk.models.DarkThemeMode
import ru.tinkoff.acquiring.sdk.models.Item
import ru.tinkoff.acquiring.sdk.models.Receipt
import ru.tinkoff.acquiring.sdk.models.enums.CheckType
import ru.tinkoff.acquiring.sdk.models.enums.Tax
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions
import ru.tinkoff.acquiring.sdk.utils.Money
import java.util.*
import javax.inject.Inject

class PayTinkoff (
    private val context: Context,
    private val fragment: OrderBottomDialog
) {
    private fun openPaymentScreen(paymentOptions: PaymentOptions) {
        TinkoffAcquiring(
            context.getString(R.string.tinkoff_terminal_key),
            context.getString(R.string.tinkoff_terminal_public_key)
        ).openPaymentScreen(
            fragment,
            paymentOptions,
            TINKOFF_PAYMENT_REQUEST_CODE
        )
    }

    fun payWithCard(amountSum: Double, receipt: Receipt, phone: String) {
        val paymentOptions = PaymentOptions().setOptions {
            orderOptions {
                orderId = Calendar.getInstance().timeInMillis.toString()
                amount = Money.ofCoins((amountSum*100).toLong())
                title = fragment.context?.getString(R.string.cron_delivery_title)
                description = "Покупки и услуги доставки"
                recurrentPayment = false
                this.receipt = receipt
            }

            customerOptions {
                customerKey = phone
                checkType = CheckType.NO.toString()
            }

            featuresOptions {
                useSecureKeyboard = true
                localizationSource = AsdkSource(Language.RU)
                handleCardListErrorInSdk = true
                darkThemeMode = DarkThemeMode.DISABLED
                emailRequired = false
                handleErrorsInSdk = true
                theme = R.style.AcquiringThemeCustom
            }
        }
        Log.d("payment", paymentOptions. toString())
        openPaymentScreen(paymentOptions)
    }

    companion object {
        private const val TINKOFF_PAYMENT_REQUEST_CODE = 1
    }
}