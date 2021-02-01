package com.dev.anhnd.mybase.utils.toast

import android.content.Context
import android.widget.Toast
import com.dev.anhnd.mybase.utils.app.runOnMainThread

fun Context.toast(message: String) {
    runOnMainThread({
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    })
}

fun Context.longToast(message: String) {
    runOnMainThread({
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    })
}