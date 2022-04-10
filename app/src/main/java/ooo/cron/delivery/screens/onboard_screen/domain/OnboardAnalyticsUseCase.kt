package ooo.cron.delivery.screens.onboard_screen.domain

import javax.inject.Inject

class OnboardAnalyticsUseCase @Inject constructor(
    private val onboardAnalyticsRepository: OnboardAnalyticsRepository
) {
    suspend fun sendMessageInAnalytics(message:String){
        onboardAnalyticsRepository.sendMessageInAnalutics(message)
    }
}