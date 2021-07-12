package ooo.cron.delivery.screens.first_address_selection_screen

import dagger.BindsInstance
import dagger.Subcomponent

/**
 * Created by Ramazan Gadzhikadiev on 14.04.2021.
 */

@Subcomponent(modules = [FirstAddressSelectionModule::class])
@FirstAddressSelectionScope
interface FirstAddressSelectionComponent {

    fun inject(activity: FirstAddressSelectionActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun context(activity: FirstAddressSelectionActivity): Builder

        fun build(): FirstAddressSelectionComponent
    }
}