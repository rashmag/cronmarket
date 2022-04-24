package ooo.cron.delivery.data.local.util

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LongPreference(
    private val pref: SharedPreferences,
    private val key: String,
    private val defaultValue: Long = 0
): ReadWriteProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return pref.getLong(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        pref.edit()
            .putLong(key, value)
            .apply()
    }
}