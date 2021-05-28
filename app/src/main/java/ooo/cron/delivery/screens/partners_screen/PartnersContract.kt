package ooo.cron.delivery.screens.partners_screen

import ooo.cron.delivery.data.network.models.PartnerCategoryRes
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.PartnersInfoRes
import ooo.cron.delivery.data.network.models.ProductCategoryModel
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
        fun showPartnerProducts(productCategoriesModel: ArrayList<ProductCategoryModel>)

        fun showBasketPreview(basketId: String, quantity: Int, basketPrice: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun getPartnerInfo()
        fun getPartnerCategory()
        fun getPartnerProducts()

        fun minusClick(product: PartnerProductsRes, position: Int)
        fun plusClick(product: PartnerProductsRes, position: Int)
        fun priceClick(product: PartnerProductsRes, position: Int)
    }
}