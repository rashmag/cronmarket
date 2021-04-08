package ooo.cron.delivery.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ooo.cron.delivery.screens.main_screen.MainComponent
import javax.inject.Singleton

/**
 * Created by Ramazan Gadzhikadiev on 06.04.2021.
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun mainComponentBuilder(): MainComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindsApplication(context: Context): Builder
    }
}