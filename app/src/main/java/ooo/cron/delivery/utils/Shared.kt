package ooo.cron.delivery.utils

import android.content.Context

/*
 * Created by Muhammad on 29.04.2021
 */



object Shared {

    private const val SHARED_PREF_NAME = "my_pref"

    fun setIntValue(
        context: Context,
        key: String?,
        value: Int
    ) {
        val editor = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setStringValue(
        context: Context,
        key: String?,
        value: String?
    ) {
        val editor = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getIntValue(context: Context, key: String?): Int {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt(key, -1)
    }

    fun getStringValue(context: Context, key: String?): String? {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(key, "default")
    }

    fun setBooleanValue(
        context: Context,
        key: String?,
        value: Boolean
    ) {
        val editor = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(
        context: Context,
        key: String?
    ): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(key, false)
    }

}