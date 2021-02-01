package com.dev.anhnd.mybase.utils.log

import android.util.Log

var APPLOG = "APP_LOG "

private val logEnable = true

//region logd debug
private fun Any.d(message: Any) {
    if (logEnable) {
        Log.d(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Any.logd(message: Any) {
    d(message)
}
//endregion

//region log error
private fun Any.e(message: Any) {
    if (logEnable) {
        Log.e(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Any.loge(message: Any) {
    e(message)
}
//endregion



//region log verbose
private fun Any.v(message: Any) {
    if (logEnable) {
        Log.v(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Any.logv(message: Any) {
    v(message)
}
//endregion

//region log info
private fun Any.i(message: Any) {
    if (logEnable) {
        Log.i(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Any.logi(message: Any) {
    i(message)
}
//endregion

//region log warning
private fun Any.w(message: Any) {
    if (logEnable) {
        Log.w(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Any.logw(message: Any) {
    w(message)
}
//endregion

//region log big error
private fun Any.wtf(message: Any) {
    if (logEnable) {
        Log.wtf(APPLOG + this::class.java.simpleName, message.toString())
    }
}

fun Any.logwtf(message: Any) {
    wtf(message)
}
//endregion
