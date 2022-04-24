package ooo.cron.delivery.data.local.util

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringPreference(
    private val pref: SharedPreferences,
    private val key: String,
    private val defaultValue: String?
): ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return pref.getString(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        pref.edit().run {
            value?.let { putString(key, it).apply() } ?: remove(key).apply()
        }
    }
}
