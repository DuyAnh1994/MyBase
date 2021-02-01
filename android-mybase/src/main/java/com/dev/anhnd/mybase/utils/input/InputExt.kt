package com.dev.anhnd.mybase.utils.input

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.dev.anhnd.mybase.utils.app.getApplication

fun hideKeyBoard(v: View) {
    val imm = getApplication().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun showKeyBoard(v: View) {
    val imm = getApplication().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}