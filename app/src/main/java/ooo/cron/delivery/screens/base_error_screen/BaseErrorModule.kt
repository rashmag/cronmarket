package ooo.cron.delivery.screens.base_error_screen

import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import ooo.cron.delivery.databinding.ActivityBaseErrorBinding

/**
 * Created by Ramazan Gadzhikadiev on 12.04.2021.
 */

@Module
class BaseErrorModule {

    @Provides
    @BaseErrorScope
    fun provideBaseErrorBinding(inflater: LayoutInflater) =
        ActivityBaseErrorBinding.inflate(inflater)
}