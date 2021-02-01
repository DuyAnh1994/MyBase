package com.dev.anhnd.mybase.utils.resource

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.dev.anhnd.mybase.utils.app.getApplication

fun getAppDrawable(@DrawableRes drawableId: Int, context: Context? = getApplication()): Drawable? {
    context?.let {
        return ContextCompat.getDrawable(context, drawableId)
    }
    return null
}

fun getAppColor(@ColorRes colorRes: Int): Int {
    return ResourcesCompat.getColor(getApplication().resources, colorRes, null)
}