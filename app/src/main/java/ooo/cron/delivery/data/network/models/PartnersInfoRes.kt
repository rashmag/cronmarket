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
    val schedule: Schedule,
    val deliveryFrames: List<DeliveryFrames> ?= null
) {

    fun map() = Partner(
        id = id,
        name = name,
        feedbackCount = feedbackCount,
        fullDescription = fullDescription.orEmpty(),
        shortDescription = shortDescription,
        logo = logo,
        mainWinImg = mainWinImg.orEmpty(),
        partnerCardImg = partnerCardImg.orEmpty(),
        minAmountDelivery = minAmountDelivery?.toDouble(),
        minAmountOrder = minAmountOrder.toDouble(),
        rating = rating,
        schedule = schedule
    )
}