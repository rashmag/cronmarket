package ooo.cron.delivery.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ooo.cron.delivery.screens.base_error_screen.BaseErrorComponent
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionComponent
import ooo.cron.delivery.screens.main_screen.MainComponent
import ooo.cron.delivery.screens.market_category_screen.MarketCategoryComponent
import javax.inject.Singleton

/**
 * Created by Ramazan Gadzhikadiev on 06.04.2021.
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun mainComponentBuilder(): MainComponent.Builder
    fun baseErrorComponentBuilder(): BaseErrorComponent.Builder
    fun firstAddressSelectionBuilder(): FirstAddressSelectionComponent.Builder
    fun marketCategoryBuilder(): MarketCategoryComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindsApplication(context: Context): Builder
    }
}