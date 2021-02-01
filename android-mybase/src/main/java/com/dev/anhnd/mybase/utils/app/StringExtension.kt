package com.dev.anhnd.mybase.utils.app

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.isEmailValid() : Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isUUIDValid() : Boolean {
    val expression = "([0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12})"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isPasswordValid():Boolean {
    if(this.length in 6..8) {
        val p = Pattern.compile("[^A-Za-z0-9]")
        val m = p.matcher(this)
        val b = m.find()
        if (!b) return true
    }
    return false
}

fun String.isFirstOrLastName():Boolean {
    if(this.length <= 50) {
        val p = Pattern.compile("[^A-Za-z0-9]")
        val m = p.matcher(this)
        val b = m.find()
        if (!b) return true
    }
    return false
}
