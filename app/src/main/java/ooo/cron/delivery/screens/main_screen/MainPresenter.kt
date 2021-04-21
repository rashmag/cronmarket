package ooo.cron.delivery.screens.main_screen

import android.util.Log
import kotlinx.coroutines.CoroutineScope
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
    private val marketCategoriesScope: CoroutineScope
) :
    BaseMvpPresenter<MainContract.View>(), MainContract.Presenter {

    private var marketCategories: List<MarketCategory>? = null

    override fun onStartView() {
        if (marketCategories != null)
            return
        
        marketCategoriesScope.launch {
            val chosenCity = dataManager.readChosenCity()
            loadMarketCategories(chosenCity.id)
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
        view?.removeMarketCategoriesProgress()
    }
}