package ooo.cron.delivery.data.network.request

/*
 * Created by Muhammad on 29.04.2021
 */



data class ConfirmCodeReq(
    var phoneNumber: String,
    var code: String,
    var basketId: String? = null
)