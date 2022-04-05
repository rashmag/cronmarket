package ooo.cron.delivery.di.screens.favorite_partners

import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import ooo.cron.delivery.databinding.FragmentFavoritePartnersBinding

/**
 * Created by Maya Nasrueva on 02.04.2022
 * */

@Module
class FavoritePartnersModule {

    @Module
    companion object {
        @Provides
        fun provideBinding(inflater: LayoutInflater) =
            FragmentFavoritePartnersBinding.inflate(inflater)
    }
}