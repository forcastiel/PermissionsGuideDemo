package com.auntec.permissionsguide.extension

import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * 和Ui相关的扩展
 * @author shrek
 * @date:  2016-05-25
 */

val View.screenX: Int
    get() {
        val array = intArrayOf(0, 0)
        getLocationOnScreen(array)
        return array[0]
    }

val View.screenY: Int
    get() {
        val array = intArrayOf(0, 0)
        getLocationOnScreen(array)
        return array[1]
    }

val View.centerX: Int
    get() {
        return screenX + width / 2
    }

val View.centerY: Int
    get() {
        return screenY + height / 2
    }

val View.rectF: RectF
    get() {
        return RectF(
            screenX.toFloat(),
            screenY.toFloat(),
            screenX + width.toFloat(),
            screenY + height.toFloat()
        )
    }

val View.rect: Rect
    get() {
        return Rect(screenX, screenY, screenX + width, screenY + height)
    }

fun View.resColor(@ColorRes colorVal: Int): Int {
    return getResources().getColor(colorVal)
}

fun View.resColorDrawable(@ColorRes colorVal: Int): Drawable {
    return ColorDrawable(resColor(colorVal))
}

private var cacheRandomID = 1000
fun View.kRandomId(): Unit {
    if (cacheRandomID >= 1000000) {
        cacheRandomID = 1000
    }
    cacheRandomID++
    id = cacheRandomID
}

fun View.setSelectorRes(
    normalDrawable: Drawable,
    selectDrawable: Drawable,
    disableDrawable: Drawable? = null
) {
    val drawable = StateListDrawable()
    //Non focused states
    drawable.addState(
        intArrayOf(
            -android.R.attr.state_focused,
            -android.R.attr.state_selected,
            -android.R.attr.state_pressed
        ),
        normalDrawable
    )
    drawable.addState(
        intArrayOf(
            -android.R.attr.state_focused,
            android.R.attr.state_selected,
            -android.R.attr.state_pressed
        ),
        selectDrawable
    )
    //Focused states
    drawable.addState(
        intArrayOf(
            android.R.attr.state_focused,
            -android.R.attr.state_selected,
            -android.R.attr.state_pressed
        ),
        selectDrawable
    )
    drawable.addState(
        intArrayOf(
            android.R.attr.state_focused,
            android.R.attr.state_selected,
            -android.R.attr.state_pressed
        ),
        selectDrawable
    )
    //Pressed
    drawable.addState(
        intArrayOf(android.R.attr.state_selected, android.R.attr.state_pressed),
        selectDrawable
    )
    drawable.addState(
        intArrayOf(android.R.attr.state_pressed),
        selectDrawable
    )
    //disable
    if (disableDrawable != null) {
        drawable.addState(intArrayOf(-android.R.attr.state_enabled), disableDrawable)
    }
    setBackgroundDrawable(drawable)
}

fun View.setSelectorRes(
    @DrawableRes normalRes: Int,
    @DrawableRes selectRes: Int,
    @DrawableRes disableRes: Int = -1
) {
    val normalDrawable = resources.getDrawable(normalRes)
    val selectDrawable = resources.getDrawable(selectRes)
    var disableDrawable: Drawable? = null
    if (disableRes != -1) disableDrawable = resources.getDrawable(disableRes)

    setSelectorRes(normalDrawable, selectDrawable, disableDrawable)
}

/**
 * 这里的颜色是颜色值  不是资源id
 */
fun View.setSelectorColor(normalColor: Int, selectColor: Int, disableColor: Int = -1) {
    val normalDrawable = ColorDrawable(normalColor)
    val selectDrawable = ColorDrawable(selectColor)
    var disableDrawable: Drawable? = null
    if (disableColor != -1) disableDrawable = ColorDrawable(disableColor)

    setSelectorRes(normalDrawable, selectDrawable, disableDrawable)
}


fun ViewGroup.each(eachDoing: (Int, View) -> Unit) {
    val num = getChildCount()
    for (position in 0..num) {
        val view = getChildAt(position)
        eachDoing(position, view)
    }
}


fun TextView.leftDrawable(resId: Int, paddingVal: Int = 0, width: Int = 0, height: Int = 0) {
    val drawable = resources.getDrawable(resId)
    drawable.setBounds(
        0,
        0,
        if (width > 0) width else drawable.minimumWidth,
        if (height > 0) height else drawable.minimumHeight
    )
    setCompoundDrawables(drawable, null, null, null)

    compoundDrawablePadding = paddingVal
}

fun TextView.rightDrawable(resId: Int, paddingVal: Int = 0) {
    val drawable = resources.getDrawable(resId)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    setCompoundDrawables(null, null, drawable, null)
    compoundDrawablePadding = paddingVal
}

fun TextView.topDrawable(resId: Int, paddingVal: Int = 0) {
    val drawable = resources.getDrawable(resId)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    setCompoundDrawables(null, drawable, null, null)

    compoundDrawablePadding = paddingVal
}

fun TextView.bottomDrawable(resId: Int, paddingVal: Int = 0) {
    val drawable = resources.getDrawable(resId)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    setCompoundDrawables(null, null, null, drawable)

    compoundDrawablePadding = paddingVal
}

fun TextView.bottomDrawable(resId: Int, paddingVal: Int = 0, width: Int = 0, height: Int = 0) {
    val drawable = resources.getDrawable(resId)

    drawable.setBounds(
        0,
        0,
        if (width > 0) width else drawable.minimumWidth,
        if (height > 0) height else drawable.minimumHeight
    )
    setCompoundDrawables(null, null, null, drawable)

    compoundDrawablePadding = paddingVal
}

/**
 * 布局参数
 */
fun Any.linearLP(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    init: LinearLayout.LayoutParams.() -> Unit = {}
)
        : LinearLayout.LayoutParams {
    val llp = LinearLayout.LayoutParams(width, height)
    llp.init()
    return llp
}

fun Any.linearLP(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    weight: Float,
    init: LinearLayout.LayoutParams.() -> Unit = {}
)
        : LinearLayout.LayoutParams {
    val llp = LinearLayout.LayoutParams(width, height)
    llp.weight = weight
    llp.init()
    return llp
}


fun Any.relativeLP(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    init: RelativeLayout.LayoutParams.() -> Unit = {}
)
        : RelativeLayout.LayoutParams {
    val rlp = RelativeLayout.LayoutParams(width, height)
    rlp.init()
    return rlp
}

fun Any.frameLP(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    init: FrameLayout.LayoutParams.() -> Unit = {}
)
        : FrameLayout.LayoutParams {
    val flp = FrameLayout.LayoutParams(width, height)
    flp.init()
    return flp
}

fun Any.viewGroupLP(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    init: ViewGroup.LayoutParams.() -> Unit = {}
)
        : ViewGroup.LayoutParams {
    val flp = ViewGroup.LayoutParams(width, height)
    flp.init()
    return flp
}

val View.windowX: Int
    get() {
        val array = intArrayOf(0, 0)
        getLocationInWindow(array)
        return array[0]
    }

val View.windowY: Int
    get() {
        val array = intArrayOf(0, 0)
        getLocationInWindow(array)
        return array[1]
    }