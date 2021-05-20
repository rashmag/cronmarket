package ooo.cron.delivery.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ooo.cron.delivery.screens.base_error_screen.BaseErrorComponent
import ooo.cron.delivery.screens.basket_screen.BasketComponent
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionComponent
import ooo.cron.delivery.screens.login_screen.login_fragments.confirm_phone_fragment.ConfirmPhoneComponent
import ooo.cron.delivery.screens.login_screen.login_fragments.enter_name_fragment.EnterNameComponent
import ooo.cron.delivery.screens.login_screen.login_fragments.enter_phone_fragment.EnterPhoneComponent
import ooo.cron.delivery.screens.main_screen.MainComponent
import ooo.cron.delivery.screens.market_category_screen.MarketCategoryComponent
import ooo.cron.delivery.screens.partners_screen.PartnersActivity
import ooo.cron.delivery.screens.partners_screen.PartnersComponent
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
    fun enterPhoneComponentBuilder(): EnterPhoneComponent.Builder
    fun confirmPhoneComponentBuilder(): ConfirmPhoneComponent.Builder
    fun enterNameComponentBuilder(): EnterNameComponent.Builder
    fun partnersComponentBuilder(): PartnersComponent.Builder
    fun basketComponentBuilder(): BasketComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindsApplication(context: Context): Builder
    }
}