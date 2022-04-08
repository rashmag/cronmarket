package ooo.cron.delivery.screens.onboard_screen.domain

import javax.inject.Inject

class OnboardAnalyticsUseCase @Inject constructor(
    private val onboardAnalyticsRep: OnboardAnalyticsRep
) {
    suspend fun sendMessageInAnalytics(message:String){
        onboardAnalyticsRep.sendMessageInAnalutics(message)
    }
}