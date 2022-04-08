package ooo.cron.delivery.di.screens.onboard

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.di.ViewModelModule
import ooo.cron.delivery.di.screens.order_history.RepositoriesModule
import ooo.cron.delivery.screens.onboard_screen.presentation.OnboardActivity

@Subcomponent(modules = [ViewModelModule::class, RepositoriesModule::class])
@OnboardScope
interface OnboardComponent {

    fun inject(activity: OnboardActivity)


    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun inflater(inflater: LayoutInflater): Builder

        fun build(): OnboardComponent
    }
}