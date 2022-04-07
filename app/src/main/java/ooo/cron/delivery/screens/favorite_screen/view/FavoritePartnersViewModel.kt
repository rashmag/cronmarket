package ooo.cron.delivery.screens.favorite_screen.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.FavoritePartners
import ooo.cron.delivery.data.network.models.Partner
import ooo.cron.delivery.screens.base.BaseViewModel
import ooo.cron.delivery.screens.favorite_screen.domain.FavoritePartnersInteractor
import ooo.cron.delivery.utils.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 02.04.2022
 * */

class FavoritePartnersViewModel @Inject constructor(
    private val interactor: FavoritePartnersInteractor,
    private val dataManager: DataManager
): BaseViewModel() {

    //получить избранных партнеров

    private val mutableFavPartners: MutableLiveData<FavoritePartners> = MutableLiveData()
    val favPartners : LiveData<FavoritePartners> get() = mutableFavPartners

    fun onStart() {
        viewModelScope.launch {
            val data = interactor.getFavoritePartners(
                cityId = dataManager.readChosenCityId()
            )
            data.process { mutableFavPartners.postValue(it)}
        }
    }
}