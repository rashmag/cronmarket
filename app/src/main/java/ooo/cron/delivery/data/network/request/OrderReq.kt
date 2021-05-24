package ooo.cron.delivery.data.network.request

/*
 * Created by Muhammad on 24.05.2021
 */



data class OrderReq(
    var basketId: String,
    var phoneNumber: String,
    var comment: String,
    var deliveryCityId: String,
    var address: String,
    var entrance: String,
    var floor: String,
    var flat: String,
    var deliverAtTime: String,
    var saveAddress: Boolean,
    var discount: Int
)