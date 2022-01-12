package ooo.cron.delivery.screens.pay_dialog_screen

sealed class PaymentVariant

object CardVariant: PaymentVariant()
object CashVariant: PaymentVariant()
object GPayVariant: PaymentVariant()