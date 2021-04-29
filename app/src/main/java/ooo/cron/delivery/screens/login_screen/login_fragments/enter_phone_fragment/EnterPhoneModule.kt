package ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment

import dagger.Binds
import dagger.Module

/*
 * Created by Muhammad on 28.04.2021
 */

@Module
interface EnterPhoneModule {
    @Binds
    @EnterPhoneScope
    fun bindPresenter(presenter: EnterPhonePresenter): EnterPhoneContract.Presenter
}