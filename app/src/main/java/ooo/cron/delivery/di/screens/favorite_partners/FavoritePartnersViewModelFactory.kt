package ooo.cron.delivery.di.screens.favorite_partners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.favorite_screen.view.FavoritePartnersViewModel
import ooo.cron.delivery.screens.favorite_screen.domain.FavoritePartnersInteractor

/**
 * Created by Maya Nasrueva on 02.04.2022
 * */

class FavoritePartnersViewModelFactory @AssistedInject constructor(
    private val interactor: FavoritePartnersInteractor,
    private val dataManager: DataManager
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FavoritePartnersViewModel::class.java)
        return FavoritePartnersViewModel(interactor, dataManager) as T
    }

    @AssistedFactory
    @FavoritePartnersScope
    interface Factory {
        fun create(): FavoritePartnersViewModelFactory
    }
}