package com.auntec.permissionsguide.ui

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.getResColor
import com.auntec.permissionsguide.ui.adaptation.CommonDevicePixel
import com.auntec.permissionsguide.ui.adaptation.PixelAdapter

abstract class BaseAct : AppCompatActivity() {

    val activityName: String by lazy {
        this.javaClass.simpleName
    }

    open var devicePixel = CommonDevicePixel._1080P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PixelAdapter.setDevelopPixel(devicePixel)
        transparentStatusBar(this)
        initView()
        initData()
    }

    abstract fun initView()

    open fun initData() {

    }

    protected fun fullScreenTheme(color: Int = getResColor(R.color.colorPrimary)) {
        val window = getWindow()
        try {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            if (Build.VERSION.SDK_INT < 21) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            if (Build.VERSION.SDK_INT >= 21) {
                window.statusBarColor = color
            }
            //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            getWindow().decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } catch (e: Exception) {
        }

    }

    protected fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = activity.window
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //view不根据系统窗口来调整自己的布局
            val mContentView = window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
            val mChildView = mContentView.getChildAt(0)
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false)
                ViewCompat.requestApplyInsets(mChildView)
            }
        }else{
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (clickBack()) return true
        }
        return super.onKeyDown(keyCode, event)
    }

    open fun clickBack(): Boolean {
        return false
    }

}