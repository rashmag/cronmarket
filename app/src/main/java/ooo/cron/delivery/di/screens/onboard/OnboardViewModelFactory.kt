package ooo.cron.delivery.di.screens.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ooo.cron.delivery.di.screens.order_history.OrderHistoryScope
import ooo.cron.delivery.di.screens.order_history.OrderHistoryViewModelFactory
import ooo.cron.delivery.screens.onboard_screen.domain.OnboardAnalyticsUseCase
import ooo.cron.delivery.screens.onboard_screen.presentation.OnboardViewModel
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryViewModel
import javax.inject.Inject
import javax.inject.Named

class OnboardViewModelFactory @Inject constructor(
    private val onboardAnalyticsUseCase: OnboardAnalyticsUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OnboardViewModel::class.java)
        return OnboardViewModel(onboardAnalyticsUseCase) as T
    }

    @OnboardScope
    @Named("Onboard")
    interface Factory {
        fun create(): OnboardViewModelFactory
    }
}