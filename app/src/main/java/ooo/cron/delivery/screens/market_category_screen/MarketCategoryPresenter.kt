package ooo.cron.delivery.screens.market_category_screen

import androidx.paging.PageKeyedDataSource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.RestService
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response

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
    private lateinit var tags: TagsResult

    override fun onViewCreated() {
        mainScope.launch {
            initMarketCategory()
            initCity()

            withErrorsHandle(
                {
                    initTags()
                    onAllTagsClick()
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() })
        }
    }

    override fun onTagClick(tag: Tag) {
        view?.showPartners(
            createDataSource { offset ->
                dataManager.getPartnersByTag(
                    city.id,
                    marketCategory.id,
                    tag.id,
                    offset
                )
            },
            PARTNER_OFFSET_STEP
        )
    }

    override fun onAllTagsClick() {
        //TODO(MOVE DATASOURCE INSTANTIATION INTO VIEW)
        view?.showPartners(
            createDataSource { offset ->
                dataManager.getAllPartners(
                    city.id,
                    marketCategory.id,
                    offset
                )
            },
            PARTNER_OFFSET_STEP
        )
    }

    private fun initMarketCategory() {
        view?.let {
            if (this::marketCategory.isInitialized.not())
                try {
                    marketCategory = MarketCategory(
                        it.getMarketCategoryId()!!,
                        it.getMarketCategoryName()!!,
                        it.getMarketCategoryImg()!!
                    )
                } catch (e: Exception) {
                    it.showAnyErrorScreen()
                }
        }
    }

    override fun onPartnerClicked(partnerId: String, isOpen: Boolean, openHours: Int?, openMinutes: Int?) {
        view?.navigatePartnerScreen(partnerId, isOpen, openHours, openMinutes)
    }

    private suspend fun initCity() {
        if (this::city.isInitialized.not()) {
            val chosenCity = dataManager.readChosenCity()
            if (chosenCity != null) {
                city = chosenCity
            }
        }
    }

    private suspend fun initTags() {
        if (this::tags.isInitialized.not())
            dataManager.getTagsResponse(city.id, marketCategory.id).handleTags()
    }

    private fun createDataSource(request: suspend (offset: Int) -> Response<PartnerResult>) =
        PartnersDataSource(
            onLoadInitial = { callback ->
                mainScope.launch {
                    withErrorsHandle(
                        {
                            request(0)
                                .handlePartners(callback, 0)
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
                            request(startOffset)
                                .handlePartners(callback, startOffset)
                        },
                        { view?.showConnectionErrorScreen() },
                        { view?.showAnyErrorScreen() }
                    )
                }
            }
        )

    private fun Response<TagsResult>.handleTags() {
        view?.let {
            if (!isSuccessful)
                return@handleTags it.showAnyErrorScreen()

            tags = this.body()!!
            it.showTags(tags)
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
                if (startOffset + PARTNER_OFFSET_STEP < body()!!.pagination.total)
                    startOffset + PARTNER_OFFSET_STEP
                else null
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
                if (startOffset + PARTNER_OFFSET_STEP < body()!!.pagination.total)
                    startOffset + PARTNER_OFFSET_STEP
                else null
            callback.onResult(body()?.partners ?: listOf(), nextOffset)
        }
    }

    companion object {
        const val PARTNER_OFFSET_STEP = RestService.PARTNERS_PAGINATION_LIMIT
    }
}