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
import ooo.cron.delivery.screens.ordering_screen.OrderComponent
import ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment.DeliveryDetailsComponent
import ooo.cron.delivery.screens.ordering_screen.order_cost_fragment.OrderCostComponent
import ooo.cron.delivery.screens.partners_screen.PartnersComponent
import ooo.cron.delivery.screens.payment_status_screen.PaymentStatusComponent
import ooo.cron.delivery.screens.splash_screen.SplashScreenComponent
import javax.inject.Singleton
import ooo.cron.delivery.di.screens.order_history_detail.OrderHistoryDetailComponent
import ooo.cron.delivery.di.screens.order_history.OrderHistoryComponent

/**
 * Created by Ramazan Gadzhikadiev on 06.04.2021.
 */

@Singleton
@Component(modules = [
    AppModule::class,
    AnalyticsModule::class
])
interface AppComponent {

    fun splashScreenBuilder(): SplashScreenComponent.Builder
    fun mainComponentBuilder(): MainComponent.Builder
    fun baseErrorComponentBuilder(): BaseErrorComponent.Builder
    fun firstAddressSelectionBuilder(): FirstAddressSelectionComponent.Builder
    fun marketCategoryBuilder(): MarketCategoryComponent.Builder
    fun enterPhoneComponentBuilder(): EnterPhoneComponent.Builder
    fun confirmPhoneComponentBuilder(): ConfirmPhoneComponent.Builder
    fun enterNameComponentBuilder(): EnterNameComponent.Builder
    fun partnersComponentBuilder(): PartnersComponent.Builder
    fun basketComponentBuilder(): BasketComponent.Builder
    fun orderingComponentBuilder(): OrderComponent.Builder
    fun deliveryDetailsComponentBuilder(): DeliveryDetailsComponent.Builder
    fun orderCostComponentBuilder(): OrderCostComponent.Builder
    fun orderComponentBuilder(): ooo.cron.delivery.screens.pay_dialog_screen.OrderComponent.Builder
    fun paymentStatusComponentBuilder(): PaymentStatusComponent.Builder
    fun orderHistoryComponentBuilder(): OrderHistoryComponent.Builder
    fun orderHistoryDetailComponentBuilder(): OrderHistoryDetailComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindsApplication(context: Context): Builder
    }
}