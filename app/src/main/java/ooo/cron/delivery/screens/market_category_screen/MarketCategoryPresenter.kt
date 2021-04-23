package ooo.cron.delivery.screens.market_category_screen

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */
class MarketCategoryPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope
) :
    BaseMvpPresenter<MarketCategoryContract.View>(),
    MarketCategoryContract.Presenter {

    private lateinit var marketCategory: MarketCategory
    private lateinit var city: City

    override fun onStartView() {
        mainScope.launch {
            initMarketCategory()
            initCity()

            withErrorsHandle(
                { dataManager.getTagsResponse(city.id, marketCategory.id).handleTags() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() })
        }
    }

    override fun onTagClick(tag: Tag) {
        mainScope.cancel()
        view?.showPartners(
            PartnersDataSource(
                onLoadInitial = { callback ->
                    mainScope.launch {
                        withErrorsHandle(
                            {
                                dataManager.getPartnersByTag(
                                    city.id,
                                    marketCategory.id,
                                    tag.id,
                                    0
                                ).handlePartners(callback, 0)
                            },
                            { view?.showConnectionErrorScreen() },
                            { view?.showAnyErrorScreen() }
                        )
                    }
                },
                onLoadAfter = { params, callback ->
                    val startOffset = params.key
                    mainScope.launch {
                        withErrorsHandle(
                            {
                                dataManager.getPartnersByTag(
                                    city.id,
                                    marketCategory.id,
                                    tag.id,
                                    startOffset
                                ).handlePartners(callback, startOffset)
                            },
                            { view?.showConnectionErrorScreen() },
                            { view?.showAnyErrorScreen() }
                        )
                    }
                }
            ),
            PREFETCH_DISTANCE,
            PARTNER_OFFSET_STEP
        )
    }

    override fun onAllTagsClick() {
        mainScope.cancel()
        //TODO(MOVE DATASOURCE INSTANTIATION INTO VIEW)
        view?.showPartners(
            PartnersDataSource(
                onLoadInitial = { callback ->
                    mainScope.launch {
                        withErrorsHandle(
                            {
                                dataManager.getAllPartners(
                                    city.id,
                                    marketCategory.id,
                                    0
                                ).handlePartners(callback, 0)
                            },
                            { view?.showConnectionErrorScreen() },
                            { view?.showAnyErrorScreen() }
                        )
                    }
                },
                onLoadAfter = { params, callback ->
                    val startOffset = params.key
                    mainScope.launch {
                        withErrorsHandle(
                            {
                                dataManager.getAllPartners(
                                    city.id,
                                    marketCategory.id,
                                    startOffset
                                ).handlePartners(callback, startOffset)
                            },
                            { view?.showConnectionErrorScreen() },
                            { view?.showAnyErrorScreen() }
                        )
                    }
                }
            ),
            PREFETCH_DISTANCE,
            PARTNER_OFFSET_STEP
        )
    }

    override fun onOtherTagsClick() {
        //TODO("Not yet implemented")
    }

    private fun initMarketCategory() {
        view?.let {
            try {
                marketCategory = MarketCategory(
                    it.getMarketCategoryId()!!,
                    it.getMarketCategoryName()!!
                )
            } catch (e: Exception) {
                it.showAnyErrorScreen()
            }
        }
    }

    private suspend fun initCity() {
        city = dataManager.readChosenCity()
    }

    private fun Response<TagsResult>.handleTags() {
        view?.let {
            if (!isSuccessful)
                return@handleTags it.showAnyErrorScreen()

            it.showTags(this.body()!!)
        }
    }

    private fun Response<PartnerResult>.handlePartners(
        callback: PageKeyedDataSource.LoadInitialCallback<Int, Partner>,
        startOffset: Int
    ) {
        if (isSuccessful) {
            callback.onResult(
                body()?.partners ?: listOf(),
                null,
                startOffset + PARTNER_OFFSET_STEP
            )
        } else {
            view?.showAnyErrorScreen()
        }
    }

    private fun Response<PartnerResult>.handlePartners(
        callback: PageKeyedDataSource.LoadCallback<Int, Partner>,
        startOffset: Int
    ) {
        if (isSuccessful) {
            val nextOffset =
                if (startOffset < body()!!.pagination.total) startOffset + PARTNER_OFFSET_STEP
                else null
            callback.onResult(body()?.partners ?: listOf(), nextOffset)
        }
    }

    companion object {
        const val PARTNER_OFFSET_STEP = RestService.PARTNERS_PAGINATION_LIMIT
        const val PREFETCH_DISTANCE = 10
    }
}