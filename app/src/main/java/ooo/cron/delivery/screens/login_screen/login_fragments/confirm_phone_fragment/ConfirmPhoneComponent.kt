package ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment

import dagger.Subcomponent

/*
 * Created by Muhammad on 28.04.2021
 */


@ConfirmPhoneScope
@Subcomponent(modules = [ConfirmPhoneModule::class])
interface ConfirmPhoneComponent {

    fun inject(confirmPhoneFragment: ConfirmPhoneFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): ConfirmPhoneComponent
    }
}