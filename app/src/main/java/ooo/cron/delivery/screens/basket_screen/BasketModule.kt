package ooo.cron.delivery.screens.basket_screen

import android.view.LayoutInflater
import dagger.Binds
import dagger.Module
import dagger.Provides
import ooo.cron.delivery.databinding.ActivityBasketBinding

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */

@Module
interface BasketModule {

    @Binds
    @BasketScope
    fun bindPresenter(presenter: BasketPresenter): BasketContract.Presenter

    @Module
    companion object {

        @Provides
        @BasketScope
        fun provideBinding(inflater: LayoutInflater) =
            ActivityBasketBinding.inflate(inflater)
    }
}