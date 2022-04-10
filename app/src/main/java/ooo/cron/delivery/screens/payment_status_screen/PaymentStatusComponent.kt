package ooo.cron.delivery.screens.payment_status_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

@PaymentStatusScope
@Subcomponent(modules = [PaymentStatusModule::class])
interface PaymentStatusComponent {

    fun inject(fragment: PaymentStatusFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): PaymentStatusComponent
    }
}