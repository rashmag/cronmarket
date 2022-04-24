package ooo.cron.delivery.di.screens.order_pay

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.screens.pay_dialog_screen.OrderBottomDialog
import ooo.cron.delivery.screens.pay_dialog_screen.OrderCommentBottomDialog
import ooo.cron.delivery.screens.pay_dialog_screen.OrderDeliveryTimeBottomSheet

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

@OrderScope
@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(fragment: OrderBottomDialog)
    fun inject(fragment: OrderCommentBottomDialog)
    fun inject(fragment: OrderDeliveryTimeBottomSheet)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun buildInstance(inflater: LayoutInflater): Builder

        fun build(): OrderComponent
    }
}