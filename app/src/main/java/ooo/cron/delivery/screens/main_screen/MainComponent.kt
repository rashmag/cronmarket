package ooo.cron.delivery.screens.main_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.screens.favorite_screen.view.FavoritePartnersFragment

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@MainScope
@Subcomponent (modules = [MainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): MainComponent
    }
}