package com.auntec.permissionsguide.ui.adaptation

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.verticalMargin
import org.jetbrains.anko.windowManager

/**
 * 像素适配
 * @author shrek
 * @date:  2018-10-19
 */
var ViewGroup.MarginLayoutParams.pixelTopMargin: Int
    get() = 0
    set(value) {
        topMargin = PixelAdapter.trueHeight(value)
    }

var ViewGroup.MarginLayoutParams.pixelBottomMargin: Int
    get() = 0
    set(value) {
        bottomMargin = PixelAdapter.trueHeight(value)
    }

var ViewGroup.MarginLayoutParams.pixelLeftMargin: Int
    get() = 0
    set(value) {
        leftMargin = PixelAdapter.trueWidth(value)
    }

var ViewGroup.MarginLayoutParams.pixelRightMargin: Int
    get() = 0
    set(value) {
        rightMargin = PixelAdapter.trueWidth(value)
    }

var ViewGroup.MarginLayoutParams.pixelVerticalMargin: Int
    get() = 0
    set(value) {
        verticalMargin = PixelAdapter.trueHeight(value)
    }

var ViewGroup.MarginLayoutParams.pixelHorizontalMargin: Int
    get() = 0
    set(value) {
        horizontalMargin = PixelAdapter.trueWidth(value)
    }


fun View.pixelPadding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    setPadding(
        PixelAdapter.trueWidth(left),
        PixelAdapter.trueHeight(top),
        PixelAdapter.trueWidth(right),
        PixelAdapter.trueHeight(bottom)
    )
}

inline fun <T : View> T.pixelLinearParams(
    width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    init: LinearLayout.LayoutParams.() -> Unit
): T {
    var trueWidth = width
    var trueHeight = height
    if (trueWidth > 0) {
        trueWidth = PixelAdapter.trueWidth(width)
    }
    if (trueHeight > 0) {
        trueHeight = PixelAdapter.trueHeight(height)
    }
    val layoutParams = LinearLayout.LayoutParams(trueWidth, trueHeight)
    layoutParams.init()
    this.layoutParams = layoutParams
    return this
}

inline fun <T : View> T.pixelLinearParams(
    width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    weight: Float,
    init: LinearLayout.LayoutParams.() -> Unit
): T {
    var trueWidth = width
    var trueHeight = height
    if (trueWidth > 0) {
        trueWidth = PixelAdapter.trueWidth(width)
    }
    if (trueHeight > 0) {
        trueHeight = PixelAdapter.trueHeight(height)
    }
    val layoutParams = LinearLayout.LayoutParams(trueWidth, trueHeight, weight)
    layoutParams.init()
    this@pixelLinearParams.layoutParams = layoutParams
    return this
}

inline fun <T : View> T.pixelRelateParams(
    width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    init: RelativeLayout.LayoutParams.() -> Unit
): T {
    var trueWidth = width
    var trueHeight = height
    if (trueWidth > 0) {
        trueWidth = PixelAdapter.trueWidth(width)
    }
    if (trueHeight > 0) {
        trueHeight = PixelAdapter.trueHeight(height)
    }
    val layoutParams = RelativeLayout.LayoutParams(trueWidth, trueHeight)
    layoutParams.init()
    this.layoutParams = layoutParams
    return this
}

inline fun <T : View> T.pixelFrameParams(
    width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    init: FrameLayout.LayoutParams.() -> Unit
): T {
    var trueWidth = width
    var trueHeight = height
    if (trueWidth > 0) {
        trueWidth = PixelAdapter.trueWidth(width)
    }
    if (trueHeight > 0) {
        trueHeight = PixelAdapter.trueHeight(height)
    }
    val layoutParams = FrameLayout.LayoutParams(trueWidth, trueHeight)
    layoutParams.init()
    this.layoutParams = layoutParams
    return this
}

inline fun <T : View> T.pixelParams(
    width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    init: ViewGroup.LayoutParams.() -> Unit
): T {
    var trueWidth = width
    var trueHeight = height
    if (trueWidth > 0) {
        trueWidth = PixelAdapter.trueWidth(width)
    }
    if (trueHeight > 0) {
        trueHeight = PixelAdapter.trueHeight(height)
    }
    val layoutParams = ViewGroup.LayoutParams(trueWidth, trueHeight)
    layoutParams.init()
    this.layoutParams = layoutParams
    return this
}

/**
 * 相对屏幕的宽度
 */
fun Context.kWidth(rate: Float = 1.0f): Float {
    return resources.displayMetrics.widthPixels * rate
}

fun Context.kIntWidth(rate: Float = 1.0f): Int {
    return kWidth(rate).toInt()
}

/**
 * 屏幕的高度
 */
fun Context.kHeight(rate: Float = 1.0f): Float {
    return resources.displayMetrics.heightPixels * rate
}

fun Context.kIntHeight(rate: Float = 1.0f): Int {
    return kHeight(rate).toInt()
}

/**
 * 屏幕的高度(全面屏含状态栏)
 */
fun View.realHeight(rate: Float = 1.0f): Float {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        return resources.displayMetrics.heightPixels * rate
    } else {
        val metrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels * rate
    }
}

fun View.kIntRealHeight(rate: Float = 1.0f): Int {
    return realHeight(rate).toInt()
}
