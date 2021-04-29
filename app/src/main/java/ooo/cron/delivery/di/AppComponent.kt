package ooo.cron.delivery.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment.ConfirmPhoneComponent
import ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment.EnterNameComponent
import ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment.EnterPhoneComponent
import ooo.cron.delivery.screens.main_screen.MainComponent
import javax.inject.Singleton

/**
 * Created by Ramazan Gadzhikadiev on 06.04.2021.
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun mainComponentBuilder(): MainComponent.Builder
    fun enterPhoneComponentBuilder(): EnterPhoneComponent.Builder
    fun confirmPhoneComponentBuilder(): ConfirmPhoneComponent.Builder
    fun enterNameComponentBuilder(): EnterNameComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindsApplication(context: Context): Builder
    }
}