package com.auntec.permissionsguide.extension

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.auntec.permissionsguide.thread.HandlerEnforcer
import com.auntec.permissionsguide.thread.ZThreadEnforcer
import kotlin.math.roundToInt

/**
 * 相对屏幕的宽度
 */
fun Any.kWidth(rate: Float = 1.0f): Float {
    return DimensAdapter.kDisplay!!.widthPixels * rate
}

fun Any.kIntWidth(rate: Float = 1.0f): Int {
    return kWidth(rate).toInt()
}

/**
 * 屏幕的高度
 */
fun Any.kHeight(rate: Float = 1.0f): Float {
    return DimensAdapter.kDisplay!!.heightPixels * rate
}

fun Any.kIntHeight(rate: Float = 1.0f): Int {
    return kHeight(rate).toInt()
}

/**
 * 屏幕的高度(全面屏含状态栏)
 */
fun Any.realHeight(rate: Float = 1.0f): Float {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        return DimensAdapter.kDisplay!!.heightPixels * rate
    } else {
        val metrics = DisplayMetrics()
        (DimensAdapter.kApplication!!.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getRealMetrics(
            metrics
        )
        return metrics.heightPixels * rate
    }
}

fun Any.kIntRealHeight(rate: Float = 1.0f): Int {
    return realHeight(rate).toInt()
}

/**
 * 主线程做什么
 */
fun Any.uiThread(delay: Long = 0, doing: () -> Unit): Unit {
    if (delay == 0L) {
        kEnforer.enforceMainThread { doing() }
    } else {
        kEnforer.enforceMainThreadDelay({
            doing()
        }, delay)
    }
}

/**
 * 其他线程做什么
 * 这里面用的是同一个线程的 消息队列
 */
fun Any.otherThread(delay:Long = 0,doing:()->Unit):Unit{
    if(delay == 0L) {
        kEnforer.enforceBackgroud { doing() }
    } else {
        kEnforer.enforceBackgroudDelay({
            doing()
        },delay)
    }
}

val Any.kEnforer: ZThreadEnforcer
    get() = HandlerEnforcer.newInstance()

fun Float.formatEndZeros(keepSize: Int = 1): String {
    if (this.compareTo(this.roundToInt()) == 0) {
        return String.format("%d", this.roundToInt())
    }
    return String.format("%.${keepSize}f", this)
}

fun Long.byteToGB(): Float {
    return this.toFloat() / 1000 / 1000 / 1000
}

fun Long.toFileSize(): String {
    var size = this
    var suffix: String? = null

    if (size >= 1024) {
        suffix = "KB"
        size /= 1024
        if (size >= 1024) {
            suffix = "MB"
            size /= 1024
        }
        if (size >= 1024) {
            suffix = "GB"
            size /= 1024
        }
    }

    val resultBuffer = StringBuilder(size.toString())

    var commaOffset = resultBuffer.length - 3
    while (commaOffset > 0) {
        resultBuffer.insert(commaOffset, ',')
        commaOffset -= 3
    }

    if (suffix != null) resultBuffer.append(suffix)
    return resultBuffer.toString()
}

fun Int.toMbps(): Float {
    return (this.toFloat() / 1000) / 1000
}

fun Long.toMbps(): Float {
    return (this.toFloat() / 1000) / 1000
}
