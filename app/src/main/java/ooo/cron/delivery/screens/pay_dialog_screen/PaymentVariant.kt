package ooo.cron.delivery.screens.pay_dialog_screen

/**
 * Created by Maya Nasrueva on 17.12.2021
 * */

sealed class PaymentVariant

object CardVariant: PaymentVariant()
object CashVariant: PaymentVariant()
object GPayVariant: PaymentVariant()