package ooo.cron.delivery.analytics

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseAnalytics @Inject constructor(
    private val analyticsTrackers: AnalyticsTrackers
): Analytics {

    private fun trackEvent(event: String, params: Map<String, Any>?) {
        analyticsTrackers.forEach {
            it.trackEvent(event, params)
        }
    }

    override fun trackOpenMainScreen(phoneNumber: String) {
        trackEvent(
            "openMain",
            mapOf(
                "phone_number" to phoneNumber
            )
        )
    }

    override fun trackOpenPartnersCard(partnerName: String, categoryName: String, phoneNumber: String) {
        val param = mapOf(
            "partner_name" to partnerName,
            "category_name" to categoryName,
            "phone_number" to phoneNumber
        )

        trackEvent(
            "openPartnerCard",
            param
        )
    }

    override fun trackOpenProductCard(productName: String, phoneNumber: String) {
        val param = mapOf(
            "product_name" to productName,
            "phone_number" to phoneNumber
        )

        trackEvent(
            "showedProductCard",
            param
        )
    }

    override fun trackOpenBasketScreen(quantity: Int, phoneNumber: String, amount: Int) {
        val param = mapOf(
            "quantity" to quantity,
            "phone_number" to phoneNumber,
            "basket_amount" to amount
        )

        trackEvent(
            "openBasket",
            param
        )
    }

    override fun trackOpenMakingOrderScreen(phoneNumber: String) {
        trackEvent(
            "beginCheckoutEvent",
            mapOf("phone_number" to phoneNumber)
        )
    }

    override fun trackMakeOrderClick(phoneNumber: String, paymentType: String) {
        val param = mapOf(
            "phone_number" to phoneNumber,
            "payment_type" to paymentType
        )

        trackEvent(
            "purchaseEvent",
            param
        )
    }
}