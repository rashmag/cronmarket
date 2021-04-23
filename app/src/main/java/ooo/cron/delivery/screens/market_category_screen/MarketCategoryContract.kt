package ooo.cron.delivery.screens.market_category_screen

import ooo.cron.delivery.data.network.models.Tag
import ooo.cron.delivery.data.network.models.TagsResult
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
interface MarketCategoryContract {
    interface View: MvpView {
        fun getMarketCategoryId(): Int?
        fun getMarketCategoryName(): String?

        fun showTags(tags: TagsResult)

        fun showPartners(dataSource: PartnersDataSource, prefetchDistance: Int, pageSize: Int)

        fun showConnectionErrorScreen()
        fun showAnyErrorScreen()
    }

    interface Presenter: MvpPresenter<View> {
        fun onStartView()
        fun onTagClick(tag: Tag)
        fun onAllTagsClick()
        fun onOtherTagsClick()
    }
}