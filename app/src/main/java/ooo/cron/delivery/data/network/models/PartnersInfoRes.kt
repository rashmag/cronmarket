package ooo.cron.delivery.data.network.models

/*
 * Created by Muhammad on 05.05.2021
 */



data class PartnersInfoRes(
    var id: String,
    var name: String,
    var fullDescription: String?,
    var shortDescription: String,
    var logo: String,
    var mainWinImg: String?,
    var partnerCardImg: String?,
    var minAmountOrder: Int,
    var minAmountDelivery: Int?,
    var rating: Float,
    var feedbackCount: Int,
    var marketCategoryId: Int,
    val schedule: Schedule
)