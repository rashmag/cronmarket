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

    var lastSelectedCity = ""

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

            if (selectedCity?.city != lastSelectedCity) {
                view?.clearAddressField()
            }
        }
    }

    override fun writeUserAddress(address: String) {
        addressScope.launch {
            dataManager.writeBuildingAddress(address)
        }
    }

    override fun onNoCitySelected() {
        view?.showAnyErrorScreen()
    }

    override fun onAddressChanged(typedAddress: String) {
        view?.disableAddressPopup()

        if (typedAddress.length > MIN_ADDRESS_LETTERS) {
            loadAddressesWhenTyping(typedAddress)
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

    private fun loadAddressesWhenTyping(addressPart: String) {
        addressScope.coroutineContext.cancelChildren()
        addressScope.launch {
            selectedCity?.let {
                withErrorsHandle(
                    { dataManager.getSuggestAddress(it.kladrId, addressPart).handleAddressesWhenTyping() },
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

    override fun setSavedAddress() {
        addressScope.launch {
            view?.showUserSavedAddress(dataManager.readBuildingAddress().orEmpty())
        }
    }

    private suspend fun Response<List<City>>.handleCities() {
        if (isSuccessful && body().isNullOrEmpty().not()) {
            cities = body()!!
            view?.showCities(cities)
            view?.showUserSavedAddress(dataManager.readBuildingAddress().orEmpty())
            lastSelectedCity = dataManager.readChosenCity().city
            view?.removeCitiesProgress()
            view?.showStartShopping()
        } else
            view?.showAnyErrorScreen()
    }

    // Вставка предложенного города, при поиске своего местоположения
    private suspend fun Response<List<SuggestAddress>>.handleAddresses() {
        if (isSuccessful && body().isNullOrEmpty().not()) {
                suggestedAddresses = body()?.weedOutNullAddresses() ?: listOf()
                if (suggestedAddresses.isEmpty().not()) {

                    suggestedAddresses.forEach {
                        view?.setFoundCity(it.city)
                        delay(DELAY_FOR_SEARCH_ADDRESS)
                        view?.setFoundAddress(it.streetWithType)
                    }
                } else {
                    view?.showWarningMessage()
                }
        } else {
            view?.showLocationNotFoundMessage()
        }
    }

    // Отображение предложенных адресов, при вводе текста
    private fun Response<List<SuggestAddress>>.handleAddressesWhenTyping() {
        if (isSuccessful && body().isNullOrEmpty().not()) {
                suggestedAddresses = body()?.weedOutNullAddresses() ?: listOf()

                if (suggestedAddresses.isEmpty().not()) {
                    view?.showAddressesPopup(suggestedAddresses)
                }else{
                    view?.showWarningMessage()
                }
            }else
                view?.showLocationNotFoundMessage()

        view?.let {
            if (it.getAddress().isValidAddress()) {
                it.showSuccessMessage()
            } else if (it.getAddress().isNotEmpty())
                it.showWarningMessage()
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
            if (it.getAddress().isValidAddress()) {
                dataManager.writeBuildingAddress(it.getAddress())
            } else {
                addressScope.launch {
                    dataManager.writeBuildingAddress(
                        if (it.getAddress() == EMPTY_ADDRESS) it.getAddress()
                        else dataManager.readBuildingAddress().toString()
                    )
                }
            }
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
        const val DELAY_FOR_SEARCH_ADDRESS = 500L
        const val EMPTY_ADDRESS = ""
    }
}