package ooo.cron.delivery.screens.onboard_screen.domain

interface OnboardAnalyticsRepository {
    suspend fun sendMessageInAnalutics(message: String)
}