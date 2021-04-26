package ooo.cron.delivery.screens.first_address_selection_screen

import ooo.cron.delivery.data.network.models.SuggestAddress
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.screens.base_mvp.MvpPresenter
import ooo.cron.delivery.screens.base_mvp.MvpView

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
interface FirstAddressSelectionContract {
    interface View : MvpView {

        fun showCities(cities: List<City>)
        fun removeCitiesProgress()

        fun showStartShopping()

        fun fillAddressField(address: String)
        fun getAddress(): String
        fun showAddressesPopup(suggestAddresses: List<SuggestAddress>)
        fun disableAddressPopup()
        fun clearAddressField()

        fun checkLocationPermission()
        fun requestLocationPermission()
        fun showLocationPermissionExplanation()
        fun checkLocationProviderEnabled()
        fun requestUserLocation()

        fun stopLocationProgress()
        fun startLocationProgress()
        fun removeLocationUpdates()
        fun showAlertGpsDisabled()

        fun disableCitySelection()
        fun disableAddressField()
        fun disableSubmitButton()

        fun navigateMainScreen()

        fun showInfoMessage()
        fun showWarningMessage()
        fun showSuccessMessage()

        fun showAnyErrorScreen()
        fun showConnectionErrorScreen()
    }

    interface Presenter: MvpPresenter<View> {
        fun onStartView()
        fun onDestroyView()

        fun onCitySelected(pos: Int)
        fun onNoCitySelected()

        fun onAddressChanged(typedAddress: String)
        fun addressItemSelected(pos: Int)

        fun onFindLocationClicked()

        fun onLocationPermissionGranted()
        fun onLocationPermissionNotGranted()
        fun onShouldShowLocationPermissionRationale()

        fun onLocationProviderEnabled()
        fun onLocationProviderDisabled()

        fun onLocationUpdated(latitude: Double, longitude: Double)

        fun onSubmitClicked()
    }
}