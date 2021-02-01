package com.dev.anhnd.mybase

import android.app.Application
import android.content.SharedPreferences


@Suppress("UNCHECKED_CAST")
class BasePreference(private val PREF_NAME: String, private val application: Application) {

    private val sharePref by lazy {
        application.getSharedPreferences(PREF_NAME, Application.MODE_PRIVATE)
    }

    fun getInstance() = sharePref

    fun <T> get(key: String, clz: Class<T>): T {
        return when (clz) {
            String::class.java -> {
                sharePref.getString(key, "") as T
            }
            Int::class.java -> {
                sharePref.getInt(key, -1) as T
            }
            Float::class.java -> {
                sharePref.getFloat(key, -1f) as T
            }
            Long::class.java -> {
                sharePref.getLong(key, -1L) as T
            }
            Boolean::class.java -> {
                sharePref.getBoolean(key, false) as T
            }
            else -> clz.newInstance()
        }
    }

    fun <T> get(key: String, defaultValue: T, clz: Class<T>): T {
        return when (clz) {
            String::class.java -> {
                sharePref.getString(key, defaultValue as String) as T
            }
            Int::class.java -> {
                sharePref.getInt(key, defaultValue as Int) as T
            }
            Float::class.java -> {
                sharePref.getFloat(key, defaultValue as Float) as T
            }
            Long::class.java -> {
                sharePref.getLong(key, defaultValue as Long) as T
            }
            Boolean::class.java -> {
                sharePref.getBoolean(key, defaultValue as Boolean) as T
            }
            else -> clz.newInstance()
        }
    }

    fun <T> put(key: String, data: T) {
        val editor: SharedPreferences.Editor = sharePref.edit()
        when (data) {
            is String -> {
                editor.putString(key, data as String)
            }
            is Boolean -> {
                editor.putBoolean(key, (data as Boolean))
            }
            is Float -> {
                editor.putFloat(key, (data as Float))
            }
            is Int -> {
                editor.putInt(key, data as Int)
            }
            is Long -> {
                editor.putLong(key, (data as Long))
            }
        }
        editor.apply()
    }
}

/**
 * Preference extension
 */
val LongClass = Long::class.java
val IntClass = Int::class.java
val StringClass = String::class.java
val FloatClass = Float::class.java
val BooleanClass = Boolean::class.java
