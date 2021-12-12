package ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/*
 * Created by Muhammad on 28.04.2021
 */

@Module
interface ConfirmPhoneModule {

    @Binds
    @ConfirmPhoneScope
    fun bindPresenter(presenter: ConfirmPhonePresenter): ConfirmPhoneContract.Presenter

    @Module
    companion object {
        @Provides
        fun provideMainScope() = CoroutineScope(Dispatchers.Main)
    }
}