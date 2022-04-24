package ooo.cron.delivery.data.local.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ListPreference<T>(
    private val pref: SharedPreferences,
    private val key: String,
    private val gson: Gson
): ReadWriteProperty<Any, MutableList<T>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableList<T> {
        val listType = object: TypeToken<List<T>>() {}.type
        return gson.fromJson(pref.getString(key, ""), listType) ?: mutableListOf()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: MutableList<T>) {
        pref.edit().putString(key, gson.toJson(value)).apply()
    }
}