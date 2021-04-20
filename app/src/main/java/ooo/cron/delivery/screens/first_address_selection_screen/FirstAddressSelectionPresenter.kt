package ooo.cron.delivery.screens.first_address_selection_screen

import kotlinx.coroutines.*
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.SuggestAddress
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
class FirstAddressSelectionPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val citiesScope: CoroutineScope,
    private val addressScope: CoroutineScope
) :
    BaseMvpPresenter<FirstAddressSelectionContract.View>(),
    FirstAddressSelectionContract.Presenter {

    private var cities: List<City> = listOf()
    private var selectedCity: City? = null

    private var suggestedAddresses: List<SuggestAddress> = listOf()

    override fun onStartView() {
        loadCities()
    }

    override fun onDestroyView() {
        citiesScope.cancel()
    }

    override fun onCitySelected(pos: Int) {
        selectedCity = cities[pos]
    }

    override fun onNoCitySelected() {
        view?.showAnyErrorScreen()
    }

    override fun onAddressChanged(typedAddress: String) {
        view?.dismissAddressPopup()

        if (typedAddress.length > MIN_ADDRESS_LETTERS) {
            loadAddresses(typedAddress)
        } else if (typedAddress.isEmpty())
            view?.showInfoMessage()
    }

    override fun onLocationUpdated(latitude: Double, longitude: Double) {
        view?.startLocationProgress()
        addressScope.launch {
            withErrorsHandle(
                { dataManager.getSuggestAddress(latitude, longitude).handleAddresses() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun addressItemSelected(pos: Int) {
        view?.fillAddressField(suggestedAddresses[pos].toString())
        view?.dismissAddressPopup()
    }

    private fun loadCities() {
        citiesScope.launch {
            withErrorsHandle(
                { dataManager.getCities().handleCities() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private fun loadAddresses(addressFragment: String) = addressScope.launch {
        selectedCity?.let {
            withErrorsHandle(
                { dataManager.getSuggestAddress(it.kladrId, addressFragment).handleAddresses() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private fun Response<List<City>>.handleCities() {
        if (isSuccessful && body().isNullOrEmpty().not()) {
            cities = body()!!
            view?.showCities(cities)
            view?.removeCitiesProgress()
            view?.showStartShopping()
        } else
            view?.showAnyErrorScreen()
    }

    private fun Response<List<SuggestAddress>>.handleAddresses() {
        if (isSuccessful && body() != null) {
            if (body()!!.isNotEmpty()) {
                suggestedAddresses = body()!!.weedOutNullAddresses()
                    view?.showAddressesPopup(suggestedAddresses)
            }

            view?.let {
                if (suggestedAddresses.firstOrNull { address ->
                        it.getAddress().contains(address.streetWithType)
                    } == null) {
                    it.showWarningMessage()
                } else
                    it.showSuccessMessage()
            }
        } else {
            view?.showAnyErrorScreen()
        }
    }

    private fun List<SuggestAddress>.weedOutNullAddresses() =
        filter { address -> address.streetWithType.isNullOrBlank().not() }

    companion object {
        const val MIN_ADDRESS_LETTERS = 2
    }
}