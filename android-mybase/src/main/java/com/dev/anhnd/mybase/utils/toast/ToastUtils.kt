package com.dev.anhnd.mybase.utils.toast

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.anhnd.mybase.utils.app.getApplication
import com.dev.anhnd.mybase.utils.app.runIfNotDestroyed
import com.dev.anhnd.mybase.utils.app.runOnMainThread

object ToastUtils {
    /**
     * Method to display short toast message
     *
     * @param context | Android Context
     * @param message | message for toast
     * */
    @JvmStatic
    fun showShortToast(message: String) {
        runOnMainThread({
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Method to display short toast message
     *
     * @param context | Android Context
     * @param messageId | string resource Id for toast
     * */
    @JvmStatic
    fun showShortToast(@StringRes messageId: Int) = showShortToast(getApplication().getString(messageId))

    /**
     * Method to display long toast message
     *
     * @param context | Android Context
     * @param message | message for toast
     * */
    @JvmStatic
    fun showLongToast(message: String) {
        runOnMainThread({
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
        })
    }

    /**
     * Method to display long toast message
     *
     * @param context | Android Context
     * @param messageId | string resource Id for toast
     * */
    @JvmStatic
    fun showLongToast(@StringRes messageId: Int) =
            showLongToast(getApplication().getString(messageId))

    @JvmStatic
    fun showErrorToast() = showShortToast("Something went wrong!")

    @JvmStatic
    fun showPermissionToast() = showShortToast("You need some permission to perform this action!")

}

/**
 * Extension function on show Long Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param message Toast message
 * */
fun Activity.showLongToast(message: String) {
    runIfNotDestroyed {
        ToastUtils.showLongToast(message)
    }
}

/**
 * Extension function on show Long Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param messageId string id resource for Toast message
 * */
fun Activity.showLongToast(@StringRes messageId: Int) = showLongToast(getString(messageId))

/**
 * Extension function on show short Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param message Toast message
 * */
fun Activity.showShortToast(message: String) {
    runIfNotDestroyed {
        ToastUtils.showShortToast(message)
    }
}

/**
 * Extension function on show short Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param messageId string id resource for Toast message
 * */
fun Activity.showShortToast(@StringRes messageId: Int) = showShortToast(getString(messageId))

/**
 * Extension function on show Long Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param message Toast message
 * */
fun AppCompatActivity.showLongToast(message: String) {
    runIfNotDestroyed {
        ToastUtils.showLongToast(message)
    }
}

/**
 * Extension function on show Long Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param messageId string id resource for Toast message
 * */
fun AppCompatActivity.showLongToast(@StringRes messageId: Int) = showLongToast(getString(messageId))

/**
 * Extension function on show short Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param message Toast message
 * */
fun AppCompatActivity.showShortToast(message: String) {
    runIfNotDestroyed {
        ToastUtils.showShortToast(message)
    }
}

/**
 * Extension function on show short Toast from Activity
 * Will run only if activity is not destroyed
 *
 * @param messageId string id resource for Toast message
 * */
fun AppCompatActivity.showShortToast(@StringRes messageId: Int) =
        showShortToast(getString(messageId))

/**
 * Extension function on show Long Toast from fragment
 *
 * @param message Toast message
 * */
fun Fragment.showLongToast(message: String) {
    ToastUtils.showLongToast(message)
}

/**
 * Extension function on show Long Toast from fragment
 *
 * @param messageId string id resource for Toast message
 * */
fun Fragment.showLongToast(@StringRes messageId: Int) = showLongToast(getString(messageId))

/**
 * Extension function on show short Toast from fragment
 *
 * @param message Toast message
 * */
fun Fragment.showShortToast(message: String) {
    ToastUtils.showShortToast(message)
}

/**
 * Extension function on show short Toast from fragment
 *
 * @param messageId string id resource for Toast message
 * */
fun Fragment.showShortToast(@StringRes messageId: Int) = showShortToast(getString(messageId))


val BeforeTAG = "ScreenRecord"

private fun tagName(name: String): String {
    val maxSizeLogTag = 23
    return try {
        when {
            name.length + BeforeTAG.length > maxSizeLogTag -> BeforeTAG + name.substring(
                    0,
                    name.length - BeforeTAG.length - 1
            )
            else -> BeforeTAG + name
        }
    } catch (e: StringIndexOutOfBoundsException) {
        BeforeTAG
    }
}
