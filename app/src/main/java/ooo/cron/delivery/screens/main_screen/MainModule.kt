package ooo.cron.delivery.screens.main_screen

import dagger.Binds
import dagger.Module

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */
@Module
interface MainModule {
    @Binds
    @MainScope
    fun bindPresenter(presenter: MainPresenter): MainContract.Presenter
}