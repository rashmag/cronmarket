package ooo.cron.delivery.screens.partners_screen

import ooo.cron.delivery.data.network.models.PartnersInfoRes
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/*
 * Created by Muhammad on 05.05.2021
 */

interface PartnersContract {

    interface View : MvpView {
        fun getPartnerId() : String
        fun showPartnerInfo(partnerInfo: PartnersInfoRes)

        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()
    }

    interface Presenter : MvpPresenter<View> {
        fun getPartnerInfo()
    }
}