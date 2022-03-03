package ooo.cron.delivery.screens.first_address_selection_screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.City
import ooo.cron.delivery.databinding.ActivityFirstAddressSelectionBinding
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.main_screen.MainActivity
import javax.inject.Inject
import ooo.cron.delivery.data.network.models.SuggestAddress
import ooo.cron.delivery.utils.enums.ReturningToScreenEnum
import ooo.cron.delivery.utils.extensions.uiLazy

/**
 * Created by Ramazan Gadzhikadiev on 13.04.2021.
 */
class FirstAddressSelectionActivity :
    BaseActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback,
    FirstAddressSelectionContract.View {

    @Inject
    lateinit var presenter: FirstAddressSelectionContract.Presenter

    @Inject
    lateinit var binding: ActivityFirstAddressSelectionBinding

    @Inject
    lateinit var addressesPopupWindow: ListPopupWindow

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var locationListener: LocationListener

    @Inject
    lateinit var locationUpdateTimer: CountDownTimer

    private var updateAddressesPopupTimer: CountDownTimer? = null

    private var returningScreen: ReturningToScreenEnum ?= null

    var citiesList = arrayListOf<String>()

    private val citiesAdapter by uiLazy {
        CitiesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        returningScreen = intent.getParcelableExtra(RETURNING_SCREEN_KEY)

        configureSelectionCity()
        configureAddressField()
        configureAddressPopup()
        configureFindLocation()
        configureSubmit()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
    }

    override fun onResume() {
        super.onResume()
        presenter.setSavedAddress()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.onLocationPermissionGranted()
                }else{
                    Toast.makeText(this, getString(R.string.common_gps_enabled), Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                Toast.makeText(this, getString(R.string.common_gps_enabled), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override suspend fun showCities(cities: List<City>) {
        updateCities(cities)
        makeCitiesVisible()
    }

    override fun removeCitiesProgress() {
        binding.root.removeView(binding.vgFirstAddressSelectionCitiesProgress.root)
    }

    override fun showStartShopping() {
        if (binding.btnFirstAddressSelectionSubmit.isVisible.not())
            binding.btnFirstAddressSelectionSubmit.visibility = View.VISIBLE
    }

    override fun fillAddressField(address: String) {
        binding.etFirstAddressSelectionAddress.apply {
            setText(address)
            setSelection(length())
        }
    }

    override fun getAddress(): String =
        binding.etFirstAddressSelectionAddress.text.toString()

    override fun setFoundAddress(suggestAddresses: String) {
        binding.etFirstAddressSelectionAddress.setText(suggestAddresses)
    }

    override fun showAddressesPopup(suggestAddresses: List<SuggestAddress>) {
        addressesPopupWindow.setAdapter(
            SuggestAddressesAdapter(this, suggestAddresses)
        )
        addressesPopupWindow.show()
    }

    override fun showUserSavedAddress(address: String) {
        binding.etFirstAddressSelectionAddress.setText(address)
    }

    override fun disableAddressPopup() {
        addressesPopupWindow.dismiss()
    }

    override fun clearAddressField() {
        with(binding) {
            etFirstAddressSelectionAddress.text?.clear()
            presenter.writeUserAddress(etFirstAddressSelectionAddress.text.toString())
            addressesPopupWindow.dismiss()
        }
    }

    override fun checkLocationPermission() {
        when {
            isLocationPermissionGranted() ->
                presenter.onLocationPermissionGranted()

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> presenter.onShouldShowLocationPermissionRationale()

            else -> presenter.onLocationPermissionNotGranted()
        }
    }

    override fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            App.LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun showLocationPermissionExplanation() {
        AlertDialog.Builder(this)
            .setTitle(R.string.first_address_selection_access_location_permission_title)
            .setMessage(R.string.first_address_selection_access_location_permission_description)
            .setPositiveButton(R.string.first_address_selection_access_location_permission_ok) { _, _ ->
                requestLocationPermission()
            }
            .create()
            .show()
    }


    override fun checkLocationProviderEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            presenter.onLocationProviderEnabled()
        } else {
            presenter.onLocationProviderDisabled()
        }
    }

    override fun requestUserLocation() {
        locationManager.requestGpsLocation(locationListener)
        locationManager.requestNetworkLocation(locationListener)
        locationUpdateTimer.start()
    }

    override fun removeLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation() =
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

    override fun startLocationUpdateTimer() {
        locationUpdateTimer.start()
    }

    override fun stopLocationUpdateTimer() {
        locationUpdateTimer.cancel()
    }

    override fun stopLocationProgress() {
        binding.tvFirstAddressSelectionFindLocation.visibility = View.VISIBLE
        binding.pbFirstAddressSelectionLocationProgress.visibility = View.GONE
    }

    override fun startLocationProgress() {
        binding.tvFirstAddressSelectionFindLocation.visibility = View.INVISIBLE
        binding.pbFirstAddressSelectionLocationProgress.visibility = View.VISIBLE
    }

    override fun showAlertGpsDisabled() {
        showAlertNoGps()
    }

    override fun disableCitySelection() {
        binding.spinnerFirstAddressSelectionCity.isEnabled = false
    }

    override fun disableAddressField() {
        binding.etFirstAddressSelectionAddress.isEnabled = false
    }

    override fun disableSubmitButton() {
        binding.btnFirstAddressSelectionSubmit.isEnabled = false
    }

    override fun navigateMainScreen() {
        when(returningScreen){
            ReturningToScreenEnum.FROM_MAIN,
            ReturningToScreenEnum.FROM_ORDERING,
            ReturningToScreenEnum.FROM_PARTNERS -> onBackPressed()

            else -> startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    override fun showInfoMessage() {
        binding.vgFirstAddressSelectionMessage.setBackgroundResource(R.color.white)
        binding.ivFirstAddressSelectionMessage.setImageResource(R.drawable.ic_first_address_selection_info)
        binding.tvFirstAddressSelectionMessage.setTextColor(color(R.color.grey90))
        binding.tvFirstAddressSelectionMessage.text =
            getString(R.string.first_address_selection_info_message)
    }

    override fun showWarningMessage() {
        binding.vgFirstAddressSelectionMessage.setBackgroundResource(R.color.question_light)
        binding.ivFirstAddressSelectionMessage.setImageResource(R.drawable.ic_warning)
        binding.tvFirstAddressSelectionMessage.setTextColor(color(R.color.grey90))
        binding.tvFirstAddressSelectionMessage.text =
            getString(R.string.first_address_selection_warning_message)
    }

    override fun showLocationNotFoundMessage() {
        binding.vgFirstAddressSelectionMessage.setBackgroundResource(R.color.false_light)
        binding.ivFirstAddressSelectionMessage.setImageResource(R.drawable.ic_first_address_selection_error)
        binding.tvFirstAddressSelectionMessage.setTextColor(color(R.color.grey90))
        binding.tvFirstAddressSelectionMessage.text =
            getString(R.string.first_address_selection_location_error_message)
    }

    override fun showSuccessMessage() {
        binding.vgFirstAddressSelectionMessage.setBackgroundResource(R.color.true_light)
        binding.ivFirstAddressSelectionMessage.setImageResource(R.drawable.ic_first_address_selection_success)
        binding.tvFirstAddressSelectionMessage.setTextColor(color(R.color.true_dark))
        binding.tvFirstAddressSelectionMessage.text =
            getString(R.string.first_address_selection_success_message)
    }

    private fun injectDependencies() {
        App.appComponent.firstAddressSelectionBuilder()
            .context(this)
            .build()
            .inject(this)
    }

    private fun configureSelectionCity() {
        binding.spinnerFirstAddressSelectionCity.adapter = citiesAdapter
        binding.spinnerFirstAddressSelectionCity.onItemSelectedListener =
            createSelectionCityListener(presenter::onCitySelected, presenter::onNoCitySelected)
        binding.spinnerFirstAddressSelectionCity.isEnabled = returningScreen != ReturningToScreenEnum.FROM_ORDERING
    }

    private fun configureAddressField() {
        binding.etFirstAddressSelectionAddress.doAfterTextChanged {
            binding.etFirstAddressSelectionAddress.background = if (it?.isNotEmpty() == true)
                ContextCompat.getDrawable(this, R.drawable.bg_address_selection_input_not_empty)
            else
                ContextCompat.getDrawable(this, R.drawable.bg_address_selection_input_empty)
            updateAddressesPopupTimer?.cancel()
            updateAddressesPopupTimer = startNewAddressUpdateTimer {
                presenter.onAddressChanged(it.toString())
            }
        }
    }

    private fun configureAddressPopup() {
        addressesPopupWindow.anchorView = binding.etFirstAddressSelectionAddress
        addressesPopupWindow.setOnItemClickListener { _, _, position, _ ->
            presenter.addressItemSelected(position)
        }
    }

    private fun configureFindLocation() {
        binding.tvFirstAddressSelectionFindLocation
            .setOnClickListener {
                presenter.onFindLocationClicked()
            }
    }

    private fun configureSubmit() {
        with(binding.btnFirstAddressSelectionSubmit) {
            setOnClickListener {
                presenter.onSubmitClicked()
            }

            text = when (returningScreen) {
                ReturningToScreenEnum.FROM_ORDERING -> getString(R.string.done_title)
                else -> getString(R.string.first_address_selection_start_shopping)
            }
        }
    }

    private fun createSelectionCityListener(
        onItemSelected: (position: Int) -> Unit,
        onNothingSelected: () -> Unit
    ) =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onItemSelected(position)
                presenter.writeCurrentCityPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                onNothingSelected()
            }
        }

    override fun setFoundCity(city: String) {
        val foundCityList = arrayListOf<String>(city)

        citiesList.forEachIndexed { i, value ->
            if (foundCityList.firstOrNull() == value) {
                binding.spinnerFirstAddressSelectionCity.setSelection(i)
            }
        }
    }

    private suspend fun updateCities(cities: List<City>) {
        with(binding.spinnerFirstAddressSelectionCity) {
            citiesList.clear()
            if (presenter.checkingFirstLaunch()) {
                setSelection(presenter.getCurrentCityPosition())
            }
            citiesAdapter.setData(cities)
            cities.forEach {
                citiesList.add(it.city)
            }
        }
    }

    private fun makeCitiesVisible() {
        if (binding.spinnerFirstAddressSelectionCity.isVisible.not())
            binding.spinnerFirstAddressSelectionCity.visibility = View.VISIBLE
    }

    private fun startNewAddressUpdateTimer(onFinish: () -> Unit) =
        object : CountDownTimer(ADDRESS_TYPE_WAITING_IN_MILLIS, ADDRESS_TYPE_WAITING_IN_MILLIS) {

            override fun onTick(millisUntilFinished: Long) {
                log("update address pop up timer ticked $millisUntilFinished")
            }

            override fun onFinish() {
                onFinish()
            }

        }.start()

    private fun isLocationPermissionGranted() =
        isFineLocationGranted() && isCoarseLocationGranted()

    private fun isFineLocationGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun isCoarseLocationGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun LocationManager.requestGpsLocation(locationListener: LocationListener) {
        requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            REQUEST_LOCATION_PERIOD_IN_MILLIS,
            REQUEST_LOCATION_DISTANCE_IN_METERS,
            locationListener
        )
    }

    @SuppressLint("MissingPermission")
    private fun LocationManager.requestNetworkLocation(locationListener: LocationListener) {
        requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            REQUEST_LOCATION_PERIOD_IN_MILLIS,
            REQUEST_LOCATION_DISTANCE_IN_METERS,
            locationListener
        )
    }

    private fun showAlertNoGps() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.common_gps_disabled))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.common_yes)) { _, _ ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(getString(R.string.common_no)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun color(@ColorRes res: Int) =
        ContextCompat.getColor(this, res)

    private fun log(message: String) {
        Log.d(this.javaClass.name, message)
    }

    companion object {
        const val REQUEST_LOCATION_PERIOD_IN_MILLIS = 10_000L
        const val REQUEST_LOCATION_DISTANCE_IN_METERS = 20F
        const val ADDRESS_TYPE_WAITING_IN_MILLIS = 300L

        const val LOCATION_REQUEST_CODE = 83

        const val RETURNING_SCREEN_KEY = "RETURNING_SCREEN_KEY"
    }
}