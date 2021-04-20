package ooo.cron.delivery.screens.first_address_selection_screen

import android.content.Context
import android.view.LayoutInflater
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
        fun provideBinder(inflater: LayoutInflater): ActivityFirstAddressSelectionBinding =
            ActivityFirstAddressSelectionBinding.inflate(inflater)

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