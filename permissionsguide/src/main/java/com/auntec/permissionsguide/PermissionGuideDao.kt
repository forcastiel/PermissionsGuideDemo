package com.auntec.permissionsguide

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import com.auntec.permissionsguide.bo.PermissionBo
import com.auntec.permissionsguide.bo.PermissionGuideConfig
import com.auntec.permissionsguide.bo.PermissionStyle

interface PermissionGuideDao {

    var kApplication: Application?
    var kDisplay: DisplayMetrics?

    var config: PermissionGuideConfig

    /**
     * 初始化屏幕适配所需
     * @param kApplication
     */
    fun init(kApplication: Application)

    /**
     *初始化开关数据，开关默认开启，已设置默认样式
     * @param applyFloatingWindow    是否申请悬浮窗
     * @param applyBackgroundPopup   是否申请后台弹出界面
     * @param applyAutoRun           是否申请自启动
     * @param applyLockScreen        是否申请锁屏显示
     * @param floatingWindowConfig   自定义申请悬浮窗权限弹窗样式
     * @param backgroundPopupConfig  自定义申请后台弹出界面权限弹窗样式
     * @param autoRunConfig          自定义申请自启动权限弹窗样式
     * @param lockScreenConfig       自定义申请锁屏显示权限弹窗样式
     */
    fun initData(applyFloatingWindow: Boolean? = true, applyBackgroundPopup: Boolean? = true, applyAutoRun: Boolean? = true, applyLockScreen: Boolean? = true,
                 floatingWindowConfig: PermissionStyle? = null, backgroundPopupConfig: PermissionStyle? = null, autoRunConfig: PermissionStyle? = null, lockScreenConfig: PermissionStyle? = null)

    /**
     * 悬浮窗权限是否开启
     * @param context
     */
    fun isFloatingWindowOpen(context: Context): Boolean

    /**
     * 后台弹出界面权限是否开启
     * @param context
     */
    fun isBackgroundPopupOpen(context: Context): Boolean

    /**
     * 自启动权限是否开启
     * @param context
     */
    fun isAutoRunOpen(context: Context): Boolean

    /**
     * 锁屏显示权限是否开启
     * @param context
     */
    fun isLockScreenOpen(context: Context): Boolean

    /**
     * 判断是否具有悬浮窗、后台弹出界面、自启动、锁屏显示、读写、设备信息权限
     * @param context
     */
    fun isAllPermissionsGranted(context: Context): Boolean

    /**
     * 跳转系统悬浮窗管理页
     * @param context
     * @param isFirstApply 是否是首次申请
     */
    fun applyFloatingWindow(act: Context, isFirstApply: Boolean ?= true)

    /**
     * 跳转系统后台弹出界面管理页
     * @param context
     * @param isFirstApply 是否是首次申请
     */
    fun applyBackgroundPopup(act: Context, isFirstApply: Boolean ?= true)

    /**
     * 跳转系统自启动管理页
     * @param context
     * @param step 0:第一步, 1:第二步, 2:第三步  ApplyStep
     * @param onResultProcess 申请返回回调
     */
    fun applyAutoRun(context: Context, step: Int, onResultProcess: (() -> Unit) ?= null)

    /**
     * 跳转系统锁屏显示管理页
     * @param context
     * @param isFirstApply 是否是首次申请
     */
    fun applyLockScreen(context: Context, isFirstApply: Boolean ?= true)

    /**
     * 按照优先级，根据本地记录的权限申请记录依次校验，选出需要申请的权限
     * @param context
     */
    fun getPermissionToShow(context: Context): PermissionBo

    /**
     * 展示申请某个权限的弹框
     * @param context
     * @param bo PermissionBo
     */
    fun showApplyPermissionTip(context: Context, bo: PermissionBo)

}