package ooo.cron.delivery.screens.first_address_selection_screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
import ooo.cron.delivery.data.network.models.SuggestAddress
import ooo.cron.delivery.databinding.ActivityFirstAddressSelectionBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.main_screen.MainActivity
import javax.inject.Inject


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

    private var updateAddressesPopupTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = when (requestCode) {
        App.LOCATION_PERMISSION_REQUEST_CODE -> findLocationIfPermissionGranted()
        else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showCities(cities: List<City>) {
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

    override fun showAddressesPopup(suggestAddresses: List<SuggestAddress>) {
        addressesPopupWindow.setAdapter(
            SuggestAddressesAdapter(this, suggestAddresses)
        )
        addressesPopupWindow.show()
    }

    override fun disableAddressPopup() {
        addressesPopupWindow.dismiss()
    }

    override fun startLocationProgress() {
        binding.tvFirstAddressSelectionFindLocation.visibility = View.VISIBLE
        binding.pbFirstAddressSelectionLocationProgress.visibility = View.GONE
    }

    override fun stopLocationProgress() {
        binding.tvFirstAddressSelectionFindLocation.visibility = View.INVISIBLE
        binding.pbFirstAddressSelectionLocationProgress.visibility = View.VISIBLE
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
        startActivity(Intent(this, MainActivity::class.java))
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
        binding.ivFirstAddressSelectionMessage.setImageResource(R.drawable.ic_first_address_selection_warning)
        binding.tvFirstAddressSelectionMessage.setTextColor(color(R.color.grey90))
        binding.tvFirstAddressSelectionMessage.text =
            getString(R.string.first_address_selection_warning_message)
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
            .bindLayoutInflater(layoutInflater)
            .build()
            .inject(this)
    }

    private fun configureSelectionCity() {
        binding.spinnerFirstAddressSelectionCity.adapter = CitiesAdapter(this)
        binding.spinnerFirstAddressSelectionCity.onItemSelectedListener =
            createSelectionCityListener(presenter::onCitySelected, presenter::onNoCitySelected)
    }

    private fun configureAddressField() {
        binding.etFirstAddressSelectionAddress.doAfterTextChanged {
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

    private fun configureFindLocation() =
        binding.tvFirstAddressSelectionFindLocation
            .setOnClickListener { checkLocationPermission() }

    private fun configureSubmit() =
        binding.btnFirstAddressSelectionSubmit
            .setOnClickListener { presenter.onSubmitClicked() }

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
            ) = onItemSelected(position)

            override fun onNothingSelected(parent: AdapterView<*>?) = onNothingSelected()
        }

    private fun updateCities(cities: List<City>) {
        (binding.spinnerFirstAddressSelectionCity.adapter as CitiesAdapter).run {
            clear()
            cities.forEachIndexed { index, city ->
                insert(city, index)
            }
            notifyDataSetChanged()
        }
    }

    private fun makeCitiesVisible() {
        if (binding.spinnerFirstAddressSelectionCity.isVisible.not())
            binding.spinnerFirstAddressSelectionCity.visibility = View.VISIBLE
    }

    private fun startNewAddressUpdateTimer(onFinish: () -> Unit) =
        object : CountDownTimer(ADDRESS_TYPE_WAITING_IN_MILLIS, ADDRESS_TYPE_WAITING_IN_MILLIS) {

            override fun onTick(millisUntilFinished: Long) =
                log("update address pop up timer ticked $millisUntilFinished")

            override fun onFinish() =
                onFinish()

        }.start()

    private fun checkLocationPermission() = when {
        isLocationPermissionGranted() ->
            requestUserLocation()

        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) -> showLocationPermissionExplanation()

        else -> requestLocationPermission()
    }

    private fun showLocationPermissionExplanation() = AlertDialog.Builder(this)
        .setTitle(R.string.first_address_selection_access_location_permission_title)
        .setMessage(R.string.first_address_selection_access_location_permission_description)
        .setPositiveButton(R.string.first_address_selection_access_location_permission_ok) { _, _ ->
            requestLocationPermission()
        }
        .create()
        .show()

    private fun requestLocationPermission() = ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        App.LOCATION_PERMISSION_REQUEST_CODE
    )

    private fun findLocationIfPermissionGranted() {
        if (isLocationPermissionGranted())
            requestUserLocation()
    }

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

    private fun requestUserLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            && isLocationPermissionGranted()
        ) {
            stopLocationProgress()
            requestUserLocation(locationManager)
        } else {
            showAlertNoGps()
        }
    }

    private fun requestUserLocation(locationManager: LocationManager) {
        locationManager.createLocationListener().also { listener ->
            locationManager.requestGpsLocation(listener)
            locationManager.requestNetworkLocation(listener)
        }
    }

    private fun LocationManager.createLocationListener() =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                presenter.onLocationUpdated(location.latitude, location.longitude)
                removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) =
                log("location status changed: $status")

            override fun onProviderDisabled(provider: String) {
                showAlertNoGps()
            }

            override fun onProviderEnabled(provider: String) {
                log("location provider enabled")
            }
        }

    @SuppressLint("MissingPermission")
    private fun LocationManager.requestGpsLocation(locationListener: LocationListener) =
        requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )

    @SuppressLint("MissingPermission")
    private fun LocationManager.requestNetworkLocation(locationListener: LocationListener) =
        requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )

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
        const val ADDRESS_TYPE_WAITING_IN_MILLIS = 1000L
    }
}