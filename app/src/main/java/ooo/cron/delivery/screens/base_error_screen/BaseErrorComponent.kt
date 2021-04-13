package ooo.cron.delivery.screens.base_error_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * Created by Ramazan Gadzhikadiev on 12.04.2021.
 */

@Subcomponent(modules = [BaseErrorModule::class])
@BaseErrorScope
interface BaseErrorComponent {

    fun inject(activity: BaseErrorActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun layoutInflater(inflater: LayoutInflater): Builder

        fun build(): BaseErrorComponent
    }
}