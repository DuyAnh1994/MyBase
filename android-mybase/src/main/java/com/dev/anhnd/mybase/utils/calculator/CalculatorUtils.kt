package com.dev.anhnd.mybase.utils.calculator

import android.content.Context
import android.util.TypedValue
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log10
import kotlin.math.pow

object CalculatorUtils {

    fun getSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return (DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble()))
                + " " + units[digitGroups])
    }

    fun getDurationMinSec(duration: Long): String {
        val min: Long = (duration / (1_000 * 60))
        val sec: Long = (duration / 1_000) % 60
        return String.format("%02d:%02d", min, sec)
    }

    fun getDurationMinSecMillis(duration: Long): String {
        val min: Long = (duration / (1_000 * 60))
        val sec: Long = (duration / 1_000) % 60
        val ms: Long = duration % 60
        return String.format("%02d:%02d:%02d", min, sec, ms)
    }

    fun getTimeAudioTrimCutout(duration: Long): String {
        val hr: Long = (duration / (1_000 * 60 * 60)) % 24
        val min: Long = (duration / (1_000 * 60))
        val sec: Long = (duration / 1_000) % 60
        val ms: Long = duration % 10
        return String.format("%02d:%02d:%02d.%02d", hr, min, sec, ms)
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.resources.displayMetrics
        ).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        return (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }
}
