package ooo.cron.delivery.screens.first_address_selection_screen

import android.location.Location
import ooo.cron.delivery.data.network.models.SuggestAddress
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
interface FirstAddressSelectionContract {
    interface View : MvpView {

        suspend fun showCities(cities: List<City>)
        fun removeCitiesProgress()

        fun showStartShopping()

        fun fillAddressField(address: String)
        fun getAddress(): String
        fun setFoundAddress(suggestAddresses: String)
        fun showAddressesPopup(suggestAddresses: List<SuggestAddress>)
        fun disableAddressPopup()
        fun clearAddressField()

        fun checkLocationPermission()
        fun requestLocationPermission()
        fun showLocationPermissionExplanation()
        fun checkLocationProviderEnabled()
        fun requestUserLocation()
        fun removeLocationUpdates()
        fun getLastKnownLocation(): Location?
        fun startLocationUpdateTimer()
        fun stopLocationUpdateTimer()

        fun stopLocationProgress()
        fun startLocationProgress()
        fun showAlertGpsDisabled()

        fun disableCitySelection()
        fun disableAddressField()
        fun disableSubmitButton()

        fun navigateMainScreen()

        fun showInfoMessage()
        fun showWarningMessage()
        fun showLocationNotFoundMessage()
        fun showSuccessMessage()
        fun showWriteHouseNumberMessage()

        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()

        fun showUserSavedAddress(address: String)

        fun setFoundCity(city: String)
    }

    interface Presenter: MvpPresenter<View> {
        fun onStartView()
        fun onDestroyView()

        fun onCitySelected(pos: Int)
        fun onNoCitySelected()

        fun onAddressChanged(typedAddress: String)
        fun addressItemSelected(pos: Int)

        fun onFindLocationClicked()

        suspend fun checkingFirstLaunch(): Boolean
        fun getCurrentCityPosition(): Int
        fun writeCurrentCityPosition(position: Int)

        fun onLocationPermissionGranted()
        fun onLocationPermissionNotGranted()
        fun onShouldShowLocationPermissionRationale()

        fun onLocationProviderEnabled()
        fun onLocationProviderDisabled()

        fun onLocationUpdated(latitude: Double, longitude: Double)
        fun onLocationProviderUpdateTimerFinished()

        fun onSubmitClicked()

        fun writeUserAddress(address: String)

        fun setSavedAddress()
    }
}