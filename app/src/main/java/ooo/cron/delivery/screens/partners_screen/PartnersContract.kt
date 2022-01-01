package ooo.cron.delivery.screens.partners_screen

import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView
import java.util.ArrayList

/*
 * Created by Muhammad on 05.05.2021
 */

interface PartnersContract {

    interface View : MvpView {
        fun getPartnerId(): String
        fun showPartnerInfo(partnerInfo: PartnersInfoRes)

        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()
        fun showPartnerCategory(body: PartnerCategoryRes)
        fun removeProgress()
        fun showPartnerProducts(productCategoriesModel: ArrayList<ProductCategoryModel>)

        fun showClearBasketDialog(onDismiss: () -> Unit, onAccept: () -> Unit)
        suspend fun updateBasketPreview(quantity: Int, basketPrice: String)

        fun showChangeAddressDialog()

        fun showChangeAddressScreen()

        fun showOrderFromDialog()

        fun getMinOrderAmount(): Int

        fun showProductInfo(product: PartnerProductsRes)

        fun navigateBasket(
            openHours: Int,
            openMinutes: Int,
            closeHours: Int,
            closeMinutes: Int,
            basket: Basket?
        )
    }

    interface Presenter : MvpPresenter<View> {
        fun getPartnerInfo()
        fun getPartnerCategory()
        fun getPartnerProducts()

        fun requiredAddressDialogAccepted()
        fun requiredAddressDialogDeclined()

        fun onBasketClicked()

        fun productClick(product: PartnerProductsRes)

        fun minusClick(
            product: PartnerProductsRes,
            quantity: Int
        )

        fun plusClick(
            product: PartnerProductsRes,
            additives: List<BasketDishAdditive>,
            quantity: Int
        )
    }
}