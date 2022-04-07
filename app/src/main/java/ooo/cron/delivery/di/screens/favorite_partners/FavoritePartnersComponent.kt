package ooo.cron.delivery.di.screens.favorite_partners

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent
import ooo.cron.delivery.screens.favorite_screen.view.FavoritePartnersFragment

/**
 * Created by Maya Nasrueva on 02.04.2022
 * */

@FavoritePartnersScope
@Subcomponent(modules = [FavoritePartnersModule::class])
interface FavoritePartnersComponent {

    fun inject(fragment: FavoritePartnersFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindInflater(inflater: LayoutInflater): Builder

        fun build(): FavoritePartnersComponent
    }
}