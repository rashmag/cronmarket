package ooo.cron.delivery.analytics

interface Analytics {

    fun trackOpenMainScreen(phoneNumber: String) = Unit
    fun trackOpenPartnersCard(partnerName: String, categoryName: String, phoneNumber: String) = Unit
    fun trackOpenProductCard(productName: String, phoneNumber: String) = Unit
    fun trackOpenBasketScreen(quantity: Int, phoneNumber: String, amount: Int) = Unit
    fun trackOpenMakingOrderScreen(phoneNumber: String) = Unit
    fun trackMakeOrderClick(phoneNumber: String, paymentType: String) = Unit
}