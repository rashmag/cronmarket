package ooo.cron.delivery.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import ooo.cron.delivery.data.local.util.StringPreference

interface PreferenceStorage {
    var partnerOpenTime: String?
    var partnerCloseTime: String?
}

class SharedPreferenceStorage @Inject constructor(context: Context): PreferenceStorage {

    private val gson = Gson()

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override var partnerOpenTime by StringPreference(prefs, PARTNER_OPEN_TIME, "")
    override var partnerCloseTime by StringPreference(prefs, PARTNER_CLOSE_TIME, "")

    companion object {
        private const val PREF_NAME = "PREF_NAME"
        private const val PARTNER_OPEN_TIME = "PARTNER_OPEN_TIME"
        private const val PARTNER_CLOSE_TIME = "PARTNER_CLOSE_TIME"
    }
}