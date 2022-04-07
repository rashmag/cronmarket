package ooo.cron.delivery.screens.onboard_screen

import dagger.Binds
import dagger.Module

@Module
interface OnboardModule {
    @Binds
    @OnboardScope
    fun bindPresenter(presenter: OnboardPresenter): OnboardContact.Presenter

}