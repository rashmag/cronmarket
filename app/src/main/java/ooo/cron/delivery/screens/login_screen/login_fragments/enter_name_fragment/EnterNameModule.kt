package ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment

import dagger.Binds
import dagger.Module

/*
 * Created by Muhammad on 28.04.2021
 */

@Module
interface EnterNameModule {
    @Binds
    @EnterNameScope
    fun bindPresenter(presenter: EnterNamePresenter): EnterNameContract.Presenter
}