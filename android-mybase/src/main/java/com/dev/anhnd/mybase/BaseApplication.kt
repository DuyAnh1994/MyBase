package com.dev.anhnd.mybase

import android.app.Application
import com.dev.anhnd.mybase.utils.app.initBaseApplication
import com.dev.anhnd.mybase.utils.app.initPrefData
import org.jetbrains.annotations.NotNull

abstract class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initBaseApplication()
        initPrefData(preferenceName())
        setUp()
    }

    abstract fun setUp()

    @NotNull
    abstract fun preferenceName(): String
}
