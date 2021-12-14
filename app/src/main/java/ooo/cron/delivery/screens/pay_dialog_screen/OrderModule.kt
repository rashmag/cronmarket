package ooo.cron.delivery.screens.pay_dialog_screen

import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import ooo.cron.delivery.databinding.DialogOrderBinding

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

@Module
interface OrderModule {

    @Module
    companion object {

        @Provides
        @OrderScope
        fun provideInflater(inflater: LayoutInflater) = DialogOrderBinding
            .inflate(inflater)
    }
}