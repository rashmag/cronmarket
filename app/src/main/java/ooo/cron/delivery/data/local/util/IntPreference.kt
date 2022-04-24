package ooo.cron.delivery.data.local.util

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class IntPreference(
    private val pref: SharedPreferences,
    private val key: String,
    private val defaultValue: Int
): ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return pref.getInt(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        pref.edit()
            .putInt(key, value)
            .apply()
    }
}