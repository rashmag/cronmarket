package ooo.cron.delivery.screens.pay_dialog_screen

import android.content.Context
import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

@OrderScope
@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(fragment: OrderBottomDialog)
    fun inject(fragment: OrderCommentBottomDialog)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun buildInstance(inflater: LayoutInflater): Builder

        fun build(): OrderComponent
    }
}