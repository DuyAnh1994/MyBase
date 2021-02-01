package com.dev.anhnd.mybase.utils.packagemanager

import android.graphics.drawable.Drawable

data class AppInfo(
    val label: String = "App name",
    val icon: Drawable? = null,
    val packageName: String = ""
)
