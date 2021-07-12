package ooo.cron.delivery.data.network.request

/*
 * Created by Muhammad on 24.05.2021
 */



data class OrderReq(
    var basketId: String? = null,
    var phoneNumber: String? = null,
    var comment: String? = null,
    var deliveryCityId: String? = null,
    var address: String? = null,
    var entrance: String? = null,
    var floor: String? = null,
    var flat: String? = null,
    var deliverAtTime: String? = null,
    var saveAddress: Boolean? = null,
    var discount: Int? = null,
    var paymentMethodId: Int = 1
)