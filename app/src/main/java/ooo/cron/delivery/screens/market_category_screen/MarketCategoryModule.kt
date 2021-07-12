package ooo.cron.delivery.screens.market_category_screen

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ooo.cron.delivery.R
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
        @MarketCategoryScope
        fun provideBinder(inflater: LayoutInflater) = FragmentMarketCategoryBinding
            .inflate(inflater)

        @Provides
        @MarketCategoryScope
        fun provideTagsAdapter(
            presenter: MarketCategoryContract.Presenter) =
            TagsAdapter(
                presenter::onTagClick,
                presenter::onAllTagsClick,
            )

        @Provides
        fun provideMainScope() = CoroutineScope(Dispatchers.Main)
    }
}