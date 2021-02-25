package com.auntec.permissionsguide.extension

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.widget.TextView
import com.auntec.permissionsguide.PermissionGuideAbility
import com.auntec.permissionsguide.uitls.DimenUtils

/**
 * @author shrek
 * @date:  2016-06-02
 */

var TextView.relateTextSize: CustomTSDimens
    get() = CustomTSDimens.SIZE_0F
    set(value) {
        textSize = DimensAdapter.textSpSize(value)
    }

var TextView.textSizeOffset: Float
    get() = 0f
    set(value) {
        textSize = DimensAdapter.textSpSizeOffset(value)
    }

object DimensAdapter: PermissionGuideAbility {

    val kApplication: Application? by lazy {
        permissionGuideDao.kApplication
    }

    val kDisplay: DisplayMetrics? by lazy {
        permissionGuideDao.kDisplay
    }

    val nav_view_marge = 10

    val nav_height: Int by lazy {
        if(kDisplay != null){
            (kDisplay!!.heightPixels / 13f).toInt()
        }else{
            0
        }
    }

    val detail_nav_height: Int by lazy {
        if(kDisplay != null){
            (kDisplay!!.heightPixels / 11f).toInt()
        }else{
            0
        }
    }

    val staus_Height: Int by lazy {
        var statusResult = 20
        kApplication?.also {
            val resourceId = it.resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusResult = it.resources.getDimensionPixelSize(resourceId);
            }
        }
        statusResult
    }

    val navigata_Height: Int by lazy {
        var result = 50
        kApplication?.also {
            val rid: Int = it.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            if (rid != 0) {
                val resourceId: Int = it.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                result = it.resources.getDimensionPixelSize(resourceId)
            } else {
                result = 0
            }
        }
        result
    }

    val tsDimensOffset = -0.5f

    //缩放因子
    val scaleFactor: Float by lazy {
        1.0f
    }


    fun dip2Px(dipVal: Float): Float {
        var result = dipVal
        kApplication?.also {
            result = DimenUtils.dpToPx(dipVal, it.resources)
        }
        return result
    }

    /**
     * dip配置
     */
    val dip1: Float by lazy {
        dip2Px(1f) * scaleFactor
    }

    val dip2: Float by lazy {
        dip2Px(2f) * scaleFactor
    }

    val dip3: Float by lazy {
        dip2Px(3f) * scaleFactor
    }

    val dip4: Float by lazy {
        dip2Px(4f) * scaleFactor
    }

    val dip5: Float by lazy {
        dip2Px(5f) * scaleFactor
    }

    val dip6: Float by lazy {
        dip2Px(6f) * scaleFactor
    }

    val dip7: Float by lazy {
        dip2Px(7f) * scaleFactor
    }

    val dip8: Float by lazy {
        dip2Px(8f) * scaleFactor
    }

    val dip9: Float by lazy {
        dip2Px(9f) * scaleFactor
    }

    val dip10: Float by lazy {
        dip2Px(10f) * scaleFactor
    }

    val dip12: Float by lazy {
        dip2Px(12f) * scaleFactor
    }

    val dip15: Float by lazy {
        dip2Px(15f) * scaleFactor
    }

    val dip20: Float by lazy {
        dip2Px(20f) * scaleFactor
    }

    val five_dip: Float by lazy {
        5 * scaleFactor
    }

    val ten_dip: Float by lazy {
        10 * scaleFactor
    }

    //获取系统的字体
    val defTextSize: Float by lazy {
        val view = TextView(kApplication!!)
        (view.textSize / kDisplay!!.scaledDensity) + tsDimensOffset
    }

    /**
     * 得到状态栏的高度
     */
    fun statusBarHeight(ctx: Context): Int {
        if (ctx is Activity) {
            val outRect = Rect()
            ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            return outRect.top
        }
        return 0
    }

    /**
     * 屏幕的比例
     */
    fun screenWidthScale(scaleVal: Float): Int {
        var result = scaleVal.toInt()
        kDisplay?.also {
            result = (it.widthPixels * scaleVal).toInt()
        }
        return result
    }

    fun screenHeightScale(scaleVal: Float): Int {
        var result = scaleVal.toInt()
        kDisplay?.also {
            result = (it.heightPixels * scaleVal).toInt()
        }
        return result
    }

    /**
     * 获得字体的大小
     */

    fun textSpSize(tsType: CustomTSDimens = CustomTSDimens.NULL): Float {
        var result = defTextSize + tsType.offsetSP
        kDisplay?.also {
            result = (defTextSize + tsType.offsetSP) * it.density / it.scaledDensity
        }
        return result
    }

    fun textPxSize(tsType: CustomTSDimens = CustomTSDimens.NULL): Float {
        var result = defTextSize + tsType.offsetSP
        kDisplay?.also {
            result = (defTextSize + tsType.offsetSP) * it.density
        }
        return result
    }

    fun textSpSizeOffset(offsetSP: Float): Float {
        return defTextSize + offsetSP
    }


}

/**
 * 自定义字体大小
 */
enum class CustomTSDimens(val offsetSP: Float) {
    SIZE_56F(56f), SIZE_28F(28f), SIZE_16F(16f),
    SIZE_8F(8f), SIZE_7F(7f), SIZE_5F(5f),
    SIZE_0F(0f), SIZE_1F(1f), SIZE_2F(2f),
    SIZE_3F(3f), SIZE_4F(4f), SIZE_L1F(-1f),
    SIZE_L2F(-2f), SIZE_L3F(-3f), SIZE_L4F(-4f),
    NULL(0f)
}