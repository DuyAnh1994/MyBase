package com.dev.anhnd.mybase.utils.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.*
import com.dev.anhnd.mybase.BaseActivity
import com.dev.anhnd.mybase.BasePreference
import kotlin.reflect.KClass


private var appInstance: Application? = null
private var basePreference: BasePreference? = null

fun Application.initBaseApplication() {
    appInstance = this
}

fun getApplication() = appInstance!!

fun Application.initPrefData(preferenceName: String) {
    basePreference = BasePreference(preferenceName, this)
}

fun <T> Class<T>.getPrefData(key: String, defaultValue: T): T =
    basePreference!!.get(key, defaultValue, this)

fun <T> putPrefData(key: String, value: T) = basePreference!!.put(key, value)

fun <T> AppCompatActivity.observer(liveData: LiveData<T>, onChange: (T?) -> Unit) {
    liveData.observe(this, Observer(onChange))
}

fun <T> Fragment.observer(liveData: LiveData<T>, onChange: (T?) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(onChange))
}

fun <E> LiveData<List<E>>.isEmptyList(): Boolean {
    return value.isNullOrEmpty()
}

fun <T> MutableLiveData<T>.postSelf() {
    postValue(this.value)
}

fun <T> MutableLiveData<T>.setSelf() {
    value = this.value
}

fun <E> MutableLiveData<E>.postIfChanged(newValue: E) {
    if (this.value != newValue) {
        postValue(this.value)
    }
}

fun <E> MutableLiveData<E>.setIfChanged(newValue: E) {
    if (this.value != newValue) {
        value = this.value
    }
}

fun MutableLiveData<Boolean>.postReverseBoolean() {
    val currentValue = value ?: false
    postValue(!currentValue)
}

fun MutableLiveData<Boolean>.setReverseBoolean() {
    val currentValue = value ?: false
    value = !currentValue
}

fun BaseActivity<*>.openAppSetting(activity: AppCompatActivity, REQ: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    activity.startActivityForResult(intent, REQ)
}

fun Any.runOnMainThread(task: () -> Any?, delayMs: Long = 0L) {
    Handler(Looper.getMainLooper()).postDelayed({
        when (this) {
            is AppCompatActivity -> {
                runIfNotDestroyed { task() }
            }
            is Fragment -> {
                runIfNotDestroyed { task() }
            }
            is Activity -> {
                runIfNotDestroyed { task() }
            }
            else -> {
                task()
            }
        }
    }, delayMs)
}

fun Activity.runIfNotDestroyed(task: () -> Any?) {
    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            !isDestroyed
        } else {
            true
        }
    ) {
        task()
    }
}

fun AppCompatActivity.runIfNotDestroyed(task: () -> Any?) {
    if (this.lifecycle.currentState != Lifecycle.State.DESTROYED) {
        task()
    }
}

fun Fragment.runIfNotDestroyed(task: () -> Any?) {
    if (this.lifecycle.currentState != Lifecycle.State.DESTROYED) {
        task()
    }
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.shareParentFragmentViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = createViewModelLazy(VM::class, { requireParentFragment().viewModelStore },
    factoryProducer ?: { requireParentFragment().defaultViewModelProviderFactory })

