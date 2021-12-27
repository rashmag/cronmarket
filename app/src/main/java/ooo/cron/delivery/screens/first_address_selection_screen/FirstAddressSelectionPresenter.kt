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
    private val addressScope: CoroutineScope,
    private val submitScope: CoroutineScope
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
        citiesScope.coroutineContext.cancelChildren()
        view?.removeLocationUpdates()
    }

    override fun onCitySelected(pos: Int) {
        if (selectedCity != cities[pos]) {
            selectedCity = cities[pos]
            suggestedAddresses = listOf()
            view?.clearAddressField()
        }
    }

    override fun onNoCitySelected() {
        view?.showAnyErrorScreen()
    }

    override fun onAddressChanged(typedAddress: String) {
        view?.disableAddressPopup()

        if (typedAddress.length > MIN_ADDRESS_LETTERS) {
            loadAddresses(typedAddress)
        } else if (typedAddress.isEmpty())
            view?.showInfoMessage()
    }

    override fun addressItemSelected(pos: Int) {
        view?.fillAddressField(suggestedAddresses[pos].toString())
        view?.disableAddressPopup()
    }

    override fun onFindLocationClicked() {
        view?.clearAddressField()
        view?.checkLocationPermission()
    }

    override fun onLocationPermissionGranted() {
        view?.checkLocationProviderEnabled()
    }

    override fun onLocationPermissionNotGranted() {
        view?.requestLocationPermission()
    }

    override fun onShouldShowLocationPermissionRationale() {
        view?.showLocationPermissionExplanation()
    }

    override fun onLocationProviderEnabled() {
        view?.startLocationProgress()
        view?.requestUserLocation()
        view?.startLocationUpdateTimer()
    }

    override fun onLocationProviderDisabled() {
        view?.showAlertGpsDisabled()
    }

    override fun onLocationUpdated(latitude: Double, longitude: Double) {
        view?.stopLocationProgress()
        view?.removeLocationUpdates()
        view?.stopLocationUpdateTimer()
        loadAddresses(latitude, longitude)
    }

    override fun onLocationProviderUpdateTimerFinished() {
        view?.removeLocationUpdates()
        view?.stopLocationProgress()
        view?.getLastKnownLocation()?.let { location ->
            loadAddresses(location.latitude, location.longitude)
        }
    }

    override fun onSubmitClicked() {
        disableInteractiveViews()
        stopUpdateView()

        submitScope.launch {
            writeData()
            view?.navigateMainScreen()
        }
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

    private fun loadAddresses(addressPart: String) {
        addressScope.coroutineContext.cancelChildren()
        addressScope.launch {
            selectedCity?.let {
                withErrorsHandle(
                    { dataManager.getSuggestAddress(it.kladrId, addressPart).handleAddresses() },
                    { view?.showConnectionErrorScreen() },
                    { view?.showAnyErrorScreen() }
                )
            }
        }
    }

    private fun loadAddresses(latitude: Double, longitude: Double) {
        addressScope.coroutineContext.cancelChildren()
        addressScope.launch {
            withErrorsHandle(
                { dataManager.getSuggestAddress(latitude, longitude).handleAddresses() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private suspend fun Response<List<City>>.handleCities() {
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
                if (suggestedAddresses.isNotEmpty()) {
                    if (suggestedAddresses.firstOrNull { it.city == selectedCity?.city } != null) {
                        view?.showAddressesPopup(suggestedAddresses)
                    } else {
                        view?.showWarningMessage()
                    }
                } else
                    view?.showLocationNotFoundMessage()
            }

            view?.let {
                if (it.getAddress().isValidAddress()) {
                    it.showSuccessMessage()
                } else if (it.getAddress().isNotEmpty())
                    it.showWarningMessage()
            }
        } else {
            view?.showAnyErrorScreen()
        }
    }

    override suspend fun checkingFirstLaunch() = dataManager.readChosenCity().city != ""

    override fun writeCurrentCityPosition(position: Int) = dataManager.writeCurrentCityPosition(position)
    override fun getCurrentCityPosition() = dataManager.readCurrentCityPosition()

    private fun disableInteractiveViews() = view?.let {
        it.disableCitySelection()
        it.disableAddressField()
        it.disableSubmitButton()
    }

    private fun stopUpdateView() {
        citiesScope.coroutineContext.cancelChildren()
        addressScope.coroutineContext.cancelChildren()
    }

    private suspend fun writeData() {
        dataManager.writeChosenCity(selectedCity!!)
        view?.let {
            if (it.getAddress().isValidAddress())
                dataManager.writeBuildingAddress(it.getAddress())
        }
    }

    @Suppress("UselessCallOnNotNull")
    private fun List<SuggestAddress>.weedOutNullAddresses() =
        filter { address -> address.streetWithType.isNullOrBlank().not() }

    private fun String.isValidAddress() =
        suggestedAddresses.firstOrNull { address ->
            contains(address.streetWithType)
        } != null

    companion object {
        const val MIN_ADDRESS_LETTERS = 2
    }
}