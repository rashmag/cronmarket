package ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment

import dagger.Subcomponent

/*
 * Created by Muhammad on 28.04.2021
 */


@EnterPhoneScope
@Subcomponent(modules = [EnterPhoneModule::class])
interface EnterPhoneComponent {

    fun inject(enterPhoneFragment: EnterPhoneFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): EnterPhoneComponent
    }
}