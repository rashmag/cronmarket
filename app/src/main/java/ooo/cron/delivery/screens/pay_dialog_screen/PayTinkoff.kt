package ooo.cron.delivery.screens.pay_dialog_screen

import android.content.Context
import ooo.cron.delivery.BuildConfig
import ooo.cron.delivery.R
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
import ru.tinkoff.acquiring.sdk.localization.AsdkSource
import ru.tinkoff.acquiring.sdk.localization.Language
import ru.tinkoff.acquiring.sdk.models.DarkThemeMode
import ru.tinkoff.acquiring.sdk.models.Receipt
import ru.tinkoff.acquiring.sdk.models.enums.CheckType
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions
import ru.tinkoff.acquiring.sdk.utils.Money
import java.util.*

/**
 * Created by Maya Nasrueva on 21.12.2021
 * */

class PayTinkoff (
    private val context: Context,
    private val fragment: OrderBottomDialog
) {
    private fun openPaymentScreen(paymentOptions: PaymentOptions) {
        TinkoffAcquiring(
            BuildConfig.tinkoff_terminal_key,
            BuildConfig.tinkoff_terminal_public_key
        ).openPaymentScreen(
            fragment,
            paymentOptions,
            TINKOFF_PAYMENT_REQUEST_CODE
        )
    }

    fun payWithCard(amountSum: Double, receipt: Receipt, phone: String) {
        openPaymentScreen(createPaymentOptions(amountSum, receipt, phone))
    }

    fun getGooglePaymentOptions(amountSum: Double, receipt: Receipt, phone: String) =
        createPaymentOptions(amountSum,receipt, phone)

    private fun createPaymentOptions(amountSum: Double, receipt: Receipt, phone: String): PaymentOptions {
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
        return paymentOptions
    }

    companion object {
        private const val TINKOFF_PAYMENT_REQUEST_CODE = 1
    }
}