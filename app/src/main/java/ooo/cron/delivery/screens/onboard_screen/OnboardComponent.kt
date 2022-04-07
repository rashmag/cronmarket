package ooo.cron.delivery.screens.onboard_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [OnboardModule::class])
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