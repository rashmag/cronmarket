package ooo.cron.delivery.screens.first_address_selection_screen

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.ActivityFirstAddressSelectionBinding

/**
 * Created by Ramazan Gadzhikadiev on 14.04.2021.
 */

@Module
interface FirstAddressSelectionModule {

    @Binds
    @FirstAddressSelectionScope
    fun bindPresenter(presenter: FirstAddressSelectionPresenter):
            FirstAddressSelectionContract.Presenter

    @Module
    companion object {
        @Provides
        @FirstAddressSelectionScope
        fun provideLocationListener(presenter: FirstAddressSelectionContract.Presenter) = object : LocationListener {
            override fun onLocationChanged(location: Location) =
                presenter.onLocationUpdated(location.latitude, location.longitude)

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d(this::javaClass.name, "location status changed: $status")
            }

            override fun onProviderDisabled(provider: String) {
                presenter.onLocationProviderDisabled()
            }

            override fun onProviderEnabled(provider: String) {
                Log.d(this::javaClass.name, "location provider enabled")
            }
        }

        @Provides
        @FirstAddressSelectionScope
        fun provideLocationManager(activity: FirstAddressSelectionActivity) =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        @Provides
        @FirstAddressSelectionScope
        fun provideBinder(activity: FirstAddressSelectionActivity): ActivityFirstAddressSelectionBinding =
            ActivityFirstAddressSelectionBinding.inflate(activity.layoutInflater)

        @Provides
        fun provideMainScope() = CoroutineScope(Dispatchers.Main)

        @Provides
        @FirstAddressSelectionScope
        fun provideListPopupWindow(context: Context) =
            ListPopupWindow(context, null).apply {
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.bg_first_address_selection_suggest_addresses_popup
                    )
                )
            }
    }
}