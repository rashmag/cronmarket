package ooo.cron.delivery.utils.enums


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class ReturningToScreenEnum : Parcelable {
    FROM_ORDERING,
    FROM_MAIN,
    FROM_PARTNERS,
    FROM_PAY_DIALOG
}