package ooo.cron.delivery.screens.payment_status_screen

import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import ooo.cron.delivery.databinding.FragmentPaymentStatusBinding

@Module
class PaymentStatusModule {

    @Module
    companion object {
        @Provides
        @PaymentStatusScope
        fun provideInflater(inflater:LayoutInflater) = FragmentPaymentStatusBinding
            .inflate(inflater)
    }
}