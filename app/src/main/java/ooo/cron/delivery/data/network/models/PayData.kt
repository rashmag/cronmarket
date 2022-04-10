package ooo.cron.delivery.data.network.models

import ru.tinkoff.acquiring.sdk.models.Receipt

data class PayData(
    val phone: String,
    val amountSum: Double,
    val receipt: Receipt
)