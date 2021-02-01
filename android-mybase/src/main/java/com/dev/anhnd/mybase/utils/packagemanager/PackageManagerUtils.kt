package com.dev.anhnd.mybase.utils.packagemanager

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.dev.anhnd.mybase.utils.app.getApplication

object PackageManagerUtils {

    private val TAG = PackageManagerUtils::class.java.simpleName

    fun getPackage(): List<AppInfo> {
        val appInfo = mutableListOf<AppInfo>()
        val pm: PackageManager = getApplication().packageManager
        val intent = Intent(Intent.ACTION_SEND, null)
        intent.type = "text/plain"
        val resolveInfo = pm.queryIntentActivities(intent, 0)
        resolveInfo.forEach { info ->
            val applicationInfo = info.activityInfo.applicationInfo
            val name = applicationInfo.loadLabel(pm)
            val icon = applicationInfo.loadIcon(pm)
            val packageName = applicationInfo.packageName
            appInfo += AppInfo(name.toString(), icon, packageName)
        }
        return appInfo
    }


}
