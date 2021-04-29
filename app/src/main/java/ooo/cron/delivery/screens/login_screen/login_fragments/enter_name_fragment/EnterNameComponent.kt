package ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment

import dagger.Subcomponent

/*
 * Created by Muhammad on 28.04.2021
 */


@EnterNameScope
@Subcomponent(modules = [EnterNameModule::class])
interface EnterNameComponent {

    fun inject(enterNameFragment: EnterNameFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): EnterNameComponent
    }
}