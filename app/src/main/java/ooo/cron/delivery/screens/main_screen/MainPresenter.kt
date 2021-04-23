package ooo.cron.delivery.screens.main_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@MainScope
class MainPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
) :
    BaseMvpPresenter<MainContract.View>(), MainContract.Presenter {

    private var marketCategories: List<MarketCategory>? = null

    override fun detachView() {
        super.detachView()
        mainScope.cancel()
    }

    override fun onStartView() {
        if (marketCategories != null)
            return

        mainScope.launch {
            val chosenCity = dataManager.readChosenCity()
            loadMarketCategories(chosenCity.id)
        }

        mainScope.launch {
            val address = dataManager.readBuildingAddress()
            if (address.isNullOrBlank().not())
                view?.showSavedAddress(address!!)
        }
    }

    override fun onClickAddress() {
        mainScope.launch {
            if (dataManager.readBuildingAddress().isNullOrBlank())
                view?.navigateFirstAddressSelection()
            else
                view?.navigateAddressSelection()
        }
    }

    private suspend fun loadMarketCategories(cityId: String) = withErrorsHandle(
        { dataManager.getMarketCategories(cityId).handleMarketCategories() },
        { view?.showConnectionErrorScreen() },
        { view?.showAnyErrorScreen() }
    )

    private fun Response<List<MarketCategory>>.handleMarketCategories() {
        if (isSuccessful.not() && body().isNullOrEmpty()) {
            view?.showAnyErrorScreen()
            return
        }

        marketCategories = body()!!
        view?.showMarketCategories(marketCategories!!)
        view?.startMarketCategoryFragment(marketCategories!!.first())
        view?.removeMarketCategoriesProgress()
    }
}