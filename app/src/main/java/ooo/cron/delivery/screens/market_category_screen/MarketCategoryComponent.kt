package ooo.cron.delivery.screens.market_category_screen

import android.view.LayoutInflater
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */

@Subcomponent (modules = [MarketCategoryModule::class])
@MarketCategoryScope
interface MarketCategoryComponent {

    fun inject(fragment: MarketCategoryFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun inflater(inflater: LayoutInflater): Builder

        fun build(): MarketCategoryComponent
    }
}