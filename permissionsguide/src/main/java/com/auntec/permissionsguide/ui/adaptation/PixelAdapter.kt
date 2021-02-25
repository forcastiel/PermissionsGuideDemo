package com.auntec.permissionsguide.ui.adaptation

import android.content.Context


/**
 * 像素适配
 * @author shrek
 * @date:  2018-10-16
 */
enum class CommonDevicePixel(var height: Int, var width: Int) {
    UNKNOWN(0, 0),
    VGA(640, 480), QVGA(320, 240), HVGA(480, 320), SVGA(800, 600),
    WVGA(800, 480), FWVGA(854, 480), _720P(1280, 720), _1080P(1920, 1080)
}

object PixelAdapter {

    private var currDeviceWidth = 0
    private var currDeviceHeight = 0

    private var devDeviceWidth = 0
    private var devDeviceHeight = 0

    private var appContext: Context? = null

    fun initContext(context: Context) {
        appContext = context
    }

    fun setupCurrPixel() {
        currDeviceWidth = appContext!!.kIntWidth(1f)
        currDeviceHeight = appContext!!.kIntHeight(1f)
    }

    fun setDevelopPixel(devicePixel: CommonDevicePixel = CommonDevicePixel._1080P) {
        setupCurrPixel()
        devDeviceWidth = devicePixel.width
        devDeviceHeight = devicePixel.height
    }

    fun setDevelopPixel(width: Int = 0, height: Int = 0) {
        setupCurrPixel()
        if (width * height == 0) {
            devDeviceWidth =
                currDeviceWidth
            devDeviceHeight =
                currDeviceHeight
        } else {
            devDeviceWidth = width
            devDeviceHeight = height
        }
    }

    fun trueWidth(width: Int): Int {
        if (devDeviceWidth != 0) {
            return (width * (currDeviceWidth * 1.0f / devDeviceWidth)).toInt()
        }
        return width
    }

    fun trueHeight(height: Int): Int {
        if (devDeviceWidth != 0) {
            return (height * (currDeviceHeight * 1.0f / devDeviceHeight)).toInt()
        }
        return height
    }

}
