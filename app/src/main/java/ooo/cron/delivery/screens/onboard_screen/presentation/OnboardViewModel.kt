package ooo.cron.delivery.screens.onboard_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.screens.onboard_screen.domain.OnboardAnalyticsUseCase
import javax.inject.Inject

class OnboardViewModel @Inject constructor(
    private val onboardAnalyticsUseCase: OnboardAnalyticsUseCase
) : ViewModel() {
    fun sendMessageInAnalytics(message: String) {
        viewModelScope.launch {
            onboardAnalyticsUseCase.sendMessageInAnalytics(message)
        }
    }
}