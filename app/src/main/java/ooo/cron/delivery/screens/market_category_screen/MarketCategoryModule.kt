package ooo.cron.delivery.screens.market_category_screen

import android.view.LayoutInflater
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.databinding.FragmentMarketCategoryBinding

/**
 * Created by Ramazan Gadzhikadiev on 22.04.2021.
 */

@Module
interface MarketCategoryModule {

    @Binds
    @MarketCategoryScope
    fun bindPresenter(presenter: MarketCategoryPresenter): MarketCategoryContract.Presenter

    @Module
    companion object {

        @Provides
        fun provideBinder(inflater: LayoutInflater) = FragmentMarketCategoryBinding
            .inflate(inflater)

        @Provides
        fun provideTagsAdapter(presenter: MarketCategoryContract.Presenter) =
            TagsAdapter(
                presenter::onTagClick,
                presenter::onAllTagsClick,
                presenter::onOtherTagsClick
            )

        @Provides
        fun provideMainScope() = CoroutineScope(Dispatchers.Main)
    }
}