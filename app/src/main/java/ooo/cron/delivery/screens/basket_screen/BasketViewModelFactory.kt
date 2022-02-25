package ooo.cron.delivery.screens.basket_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ooo.cron.delivery.data.BasketInteractor

/**
* Created by Maya Nasrueva on 14.01.2022
 * */

class BasketViewModelFactory @AssistedInject constructor(
    private val interactor: BasketInteractor
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == BasketViewModel::class.java)
        return BasketViewModel(interactor) as T
    }

    @AssistedFactory
    @BasketScope
    interface Factory {
        fun create(): BasketViewModelFactory
    }

}