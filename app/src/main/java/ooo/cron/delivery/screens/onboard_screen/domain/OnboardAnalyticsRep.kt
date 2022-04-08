package ooo.cron.delivery.screens.onboard_screen.domain

interface OnboardAnalyticsRep {
    suspend fun sendMessageInAnalutics(message:String)
}