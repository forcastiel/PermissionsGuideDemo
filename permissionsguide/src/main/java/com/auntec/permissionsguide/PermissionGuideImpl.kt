package com.auntec.permissionsguide

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.auntec.permissionsguide.bo.*
import com.auntec.permissionsguide.extension.*
import com.auntec.permissionsguide.ui.PermissionsGuideAct
import com.auntec.permissionsguide.ui.PermissionsTipAct
import com.auntec.permissionsguide.ui.adaptation.PixelAdapter
import com.auntec.permissionsguide.ui.custom.*
import com.auntec.permissionsguide.uitls.autorun.AutoRunUtils
import com.auntec.permissionsguide.uitls.backgroundstart.BackgroundStartUtils
import com.auntec.permissionsguide.uitls.floatingwindow.FloatingWindowUtils
import com.auntec.permissionsguide.uitls.floatingwindow.OnPermissionResult
import com.auntec.permissionsguide.uitls.floatingwindow.rom.RomUtils
import com.auntec.permissionsguide.uitls.lockscreen.LockScreenUtils
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult


class PermissionGuideImpl : PermissionGuideDao{

    override var kApplication: Application ?= null
    override var kDisplay: DisplayMetrics ?= null

    override var config: PermissionGuideConfig = PermissionGuideConfig()

    override fun init(kApplication: Application) {
        //屏幕适配所需
        this.kApplication = kApplication
        kDisplay = this.kApplication?.resources?.displayMetrics
        PixelAdapter.initContext(kApplication)
    }

    override fun initData(applyFloatingWindow: Boolean?, applyBackgroundPopup: Boolean?, applyAutoRun: Boolean?, applyLockScreen: Boolean?,
                          floatingWindowConfig: PermissionStyle?, backgroundPopupConfig: PermissionStyle?, autoRunConfig: PermissionStyle?, lockScreenConfig: PermissionStyle?) {
        config.applyFloatingWindow = applyFloatingWindow ?: true
        config.applyBackgroundPopup = applyBackgroundPopup ?: true
        config.applyAutoRun = applyAutoRun ?: true
        config.applyLockScreen = applyLockScreen ?: true

        config.floatingWindowConfig = floatingWindowConfig ?: PermissionStyle(1, "", "升级测速诊断能力", "请授权开启悬浮窗权限，享受完整手机网络测速诊断服务", "立即开启", R.drawable.ic_xuanfuchuang)
        config.backgroundPopupConfig = backgroundPopupConfig ?: PermissionStyle(1, "", "重要通知开启提示", "检测未开启后台弹出界面权限，防止错过重要通知，建议您立即开启", "立即开启", R.drawable.ic_tishi)
        config.autoRunConfig = autoRunConfig ?: PermissionStyle(1, "", "WiFi安全感知系统", "全面兼容WiFi安全感知系统，操作更流畅，建议立即打开自启动权限", "立即开启", R.drawable.ic_ziqidong)
        config.lockScreenConfig = lockScreenConfig ?: PermissionStyle(1, "", "WiFi节能组件升级", "精准WiFi节能升级，增强测速诊断能力，更智能高效", "立即开启", R.drawable.ic_suoping)
    }

    override fun isFloatingWindowOpen(context: Context): Boolean {
        return FloatingWindowUtils.checkPermission(context)
    }

    override fun isBackgroundPopupOpen(context: Context): Boolean {
        return BackgroundStartUtils.checkPermission(context)
    }

    override fun isAutoRunOpen(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(PermissionBo.AUTO_RUN.label, false)
    }

    override fun isLockScreenOpen(context: Context): Boolean {
        return LockScreenUtils.checkPermission(context)
    }

    override fun isAllPermissionsGranted(context: Context): Boolean {
        return isFloatingWindowOpen(context) && isBackgroundPopupOpen(context) && isAutoRunOpen(context) && isLockScreenOpen(context)
                && context.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && context.checkPermission(Manifest.permission.READ_PHONE_STATE)
    }

    private fun isAllNewPermissionsGranted(context: Context): Boolean {
        return isFloatingWindowOpen(context) && isBackgroundPopupOpen(context) && isAutoRunOpen(context) && isLockScreenOpen(context)
    }

    override fun applyFloatingWindow(act: Context, isFirstApply: Boolean ?) {
        //跳转悬浮窗系统页面，申请悬浮窗权限
        FloatingWindowUtils.requestPermission(act as Activity, object : OnPermissionResult {
            override fun permissionResult(isOpen: Boolean) {
                if (isOpen) {
                    //悬浮窗已开启
                    ZsxTip(act).applyFloatingWindowSuccessDialog()
                }else{
                    //悬浮窗未开启
                    if(isFirstApply == true){
                        ZsxTip(act).permissionStyle1Dialog("", R.drawable.icon_xuanfuchuang_fail, "权限开启失败", "请开启悬浮窗权限，升级网络测速诊断能力", "去修复"){
                            applyFloatingWindow(act, false)
                        }
                    }
                }
            }
        })
        showTransTip(act, PermissionBo.FLOATING_WINDOW)
    }

    override fun applyBackgroundPopup(act: Context, isFirstApply: Boolean ?) {
        //跳转后台界面弹出系统页面，申请后台界面弹出权限
        BackgroundStartUtils.requestPermission(act as Activity, object : OnPermissionResult {
            override fun permissionResult(isOpen: Boolean) {
                if (isOpen) {
                    //后台界面弹出权限已开启
                    act.toastLongShow("权限开启成功！")
                }else{
                    //后台界面弹出权限未开启
                    if(isFirstApply == true){
                        ZsxTip(act).permissionStyle2Dialog("", R.drawable.ic_tishi_fail, "部分能力开启失败", "再次开启，享受顶级测速诊断能力", "立即开启"){
                            applyBackgroundPopup(act, false)
                        }
                    }
                }
            }
        })
        showTransTip(act, PermissionBo.BACKGROUND_POPUP)
    }

    override fun applyAutoRun(act: Context, step: Int, onResultProcess: (() -> Unit)?) {
        var isTipShowing = false
        AutoRunUtils.requestPermission(act as Activity, object : OnPermissionResult {
            override fun permissionResult(isOpen: Boolean) {
                Log.e("ttt","====>applyAutoRun permissionResult()...")
                //三星机型上该回调会执行两次
                if(isTipShowing){ return }
                isTipShowing = true
                when(step){
                    ApplyStep.FIRST_APPLY.value -> {
                        //第一次申请权限返回
                        ZsxTip(act).chooseDialog("是否已开启权限？", "只有当你成功完成设置，才可以提升网络测速诊断服务", "已开启权限", "未开启权限"){
                            isTipShowing = false
                            if(it){
                                act.startActivityForResult<PermissionsGuideAct>(PermissionsGuideAct.REQUEST_CODE1, Const.BO to PermissionBo.AUTO_RUN)
                            }else{
                                //本地记录自启动权限已开启
                                recordAutoRunPermissionGranted(act)
                                onResultProcess?.invoke()
                            }
                        }
                    }
                    ApplyStep.SECOND_APPLY.value -> {
                        //第二次申请权限返回
                        ZsxTip(act).chooseDialog("是否已开启权限？", "只有当你成功完成设置，才可以提升网络测速诊断服务", "已开启权限", "未开启权限"){
                            isTipShowing = false
                            if(it){
                                ZsxTip(act).permissionStyle2Dialog("", R.drawable.ic_tishi_fail, "部分能力开启失败", "再次开启，享受顶级测速诊断能力", "立即开启"){
                                    act.startActivityForResult<PermissionsGuideAct>(PermissionsGuideAct.REQUEST_CODE2, Const.BO to PermissionBo.AUTO_RUN)
                                }
                            }else{
                                //本地记录自启动权限已开启
                                recordAutoRunPermissionGranted(act)
                                onResultProcess?.invoke()
                            }
                        }
                    }
                    ApplyStep.THIRD_APPLY.value -> {
                        //第三次申请权限返回
                        ZsxTip(act).chooseDialog("是否已开启权限？", "只有当你成功完成设置，才可以提升CAD看图测量服务", "已开启权限", "未开启权限"){
                            if(!it){
                                //本地记录自启动权限已开启
                                recordAutoRunPermissionGranted(act)
                                onResultProcess?.invoke()
                            }
                        }
                    }
                }
            }
        })
        if(step == ApplyStep.FIRST_APPLY.value){
            showTransTip(act, PermissionBo.AUTO_RUN)
        }
    }

    override fun applyLockScreen(act: Context, isFirstApply: Boolean ?) {
        LockScreenUtils.requestPermission(act as Activity, object : OnPermissionResult {
            override fun permissionResult(isOpen: Boolean) {
                if (isOpen) {
                    //锁屏显示权限已开启
                    act.toastLongShow("权限开启成功！")
                }else{
                    //锁屏显示权限未开启
                    if(isFirstApply == true){
                        ZsxTip(act).permissionStyle2Dialog("", R.drawable.ic_tishi_fail, "部分能力开启失败", "再次开启，享受顶级测速诊断能力", "立即开启"){
                            applyLockScreen(act, false)
                        }
                    }
                }
            }
        })
        showTransTip(act, PermissionBo.LOCK_SCREEN)
    }

    /**
     * 展示透明引导页，由于vivo后台判定的问题，vivo采取自定义toast方式展示引导，后续如果还有其他机型如此可在此扩展
     */
    fun showTransTip(context: Context, permissionBo: PermissionBo){
        uiThread(200) {
            when(permissionBo){
                PermissionBo.FLOATING_WINDOW -> {
                    if(RomUtils.checkIsHuaweiRom()){
                        //已验证 荣耀V9、华为Mate9
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsMiuiRom()){
                        //已验证 小米8
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsSamSungRom()){
                        //已验证 三星s8
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsMeizuRom()){
                        //已验证 魅族16X
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsVivoRom()){
                        //已验证 vivo X9s、vivo Y93s、vivo X20A
                        if(isBackgroundPopupOpen(context)){
                            gotoPermissionsTipAct(context, permissionBo)
                        }else{
                            showCustomToast(context, permissionBo)
                        }
                    }else if(RomUtils.checkIsOppoRom()){
                        //已验证
                        //oppoA73、R11s：自定义toast、透明引导页都不支持，需要开启悬浮窗权限才行；
                        //oppoR17、Reno：支持透明引导页，在R17上偶尔第一次引导页会显示在应用内部，再次打开即正常；
                        gotoPermissionsTipAct(context, permissionBo)
                    }else{
                        gotoPermissionsTipAct(context, permissionBo)
                    }
                }
                PermissionBo.BACKGROUND_POPUP -> {
                    if(RomUtils.checkIsMiuiRom()){
                        //已验证 小米8
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsVivoRom()){
                        //已验证 vivo X9s、vivo Y93s、vivo X20A
                        if(isBackgroundPopupOpen(context)){
                            gotoPermissionsTipAct(context, permissionBo)
                        }else{
                            showCustomToast(context, permissionBo)
                        }
                    }
                }
                PermissionBo.AUTO_RUN -> {
                    if(RomUtils.checkIsHuaweiRom()){
//                        showCustomToast(context, permissionBo)
                        //已验证 荣耀V9、华为Mate9、麦芒8、荣耀Play3、华为nova
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsMiuiRom()){
                        //已验证 小米8
                        showCustomToast(context, permissionBo)
                    }else if(RomUtils.checkIsSamSungRom()){
                        //已验证 三星s8
                        showCustomToast(context, permissionBo)
                    }else if(RomUtils.checkIsMeizuRom()){
                        //已验证 魅族16X
                        showCustomToast(context, permissionBo)
                    }else if(RomUtils.checkIsVivoRom()){
                        //已验证 vivo X9s、vivo Y93s、vivo X20A
                        showCustomToast(context, permissionBo)
                    }else if(RomUtils.checkIsOppoRom()){
                        //已验证 oppo R17跳转自启动页面不对，oppo R11s正常
                        gotoPermissionsTipAct(context, permissionBo)
                    }else{
                        showCustomToast(context, permissionBo)
                    }
                }
                PermissionBo.LOCK_SCREEN -> {
                    if(RomUtils.checkIsMiuiRom()){
                        //已验证 小米8
                        gotoPermissionsTipAct(context, permissionBo)
                    }else if(RomUtils.checkIsVivoRom()){
                        //已验证 vivo X9s、vivo Y93s、vivo X20A
                        if(isBackgroundPopupOpen(context)){
                            gotoPermissionsTipAct(context, permissionBo)
                        }else{
                            showCustomToast(context, permissionBo)
                        }
                    }
                }
            }
        }
    }

    private fun gotoPermissionsTipAct(context: Context, permissionBo: PermissionBo){
        context.startActivity<PermissionsTipAct>(Const.BO to permissionBo)
    }

    private fun showCustomToast(context: Context, permissionBo: PermissionBo) {
        var view: View = FloatingWindowTipView(context)
        when(permissionBo){
            PermissionBo.FLOATING_WINDOW -> view = FloatingWindowTipView(context)
            PermissionBo.BACKGROUND_POPUP -> view = BackgroundPopupTipView(context)
            PermissionBo.AUTO_RUN -> view = AutoRunTipView(context)
            PermissionBo.LOCK_SCREEN -> view = LockScreenTipView(context)
        }
        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }

    private fun recordCount(context: Context, bo: PermissionBo) {
        //保存当天权限申请次数
        getSharedPreferences(context).edit().putInt(bo.key(), getSharedPreferences(context).getInt(bo.key(), 0) + 1).commit()
    }

    private fun recordAutoRunPermissionGranted(context: Context) {
        //保存自启动权限已获取
        getSharedPreferences(context).edit().putBoolean(PermissionBo.AUTO_RUN.label, true).commit()
    }

    private fun getApplyCount(context: Context, bo: PermissionBo): Int {
        return getSharedPreferences(context).getInt(bo.key(), 0)
    }

    override fun getPermissionToShow(context: Context): PermissionBo {
        if(isAllNewPermissionsGranted(context)){
            return PermissionBo.NULL
        }else{
            if(!isFloatingWindowOpen(context) && getApplyCount(context, PermissionBo.FLOATING_WINDOW) < config.floatingWindowConfig.limit){
                return PermissionBo.FLOATING_WINDOW
            }
            if(!isBackgroundPopupOpen(context) && getApplyCount(context, PermissionBo.BACKGROUND_POPUP) < config.backgroundPopupConfig.limit){
                return PermissionBo.BACKGROUND_POPUP
            }
            if(!isAutoRunOpen(context) && getApplyCount(context, PermissionBo.AUTO_RUN) < config.autoRunConfig.limit){
                return PermissionBo.AUTO_RUN
            }
            if(!isLockScreenOpen(context) && getApplyCount(context, PermissionBo.LOCK_SCREEN) < config.lockScreenConfig.limit){
                return PermissionBo.LOCK_SCREEN
            }
            return PermissionBo.NULL
        }
    }

    override fun showApplyPermissionTip(context: Context, bo: PermissionBo) {
        var permissionStyle = PermissionStyle()
        when(bo){
            PermissionBo.FLOATING_WINDOW -> permissionStyle = config.floatingWindowConfig
            PermissionBo.BACKGROUND_POPUP -> permissionStyle = config.backgroundPopupConfig
            PermissionBo.AUTO_RUN -> permissionStyle = config.autoRunConfig
            PermissionBo.LOCK_SCREEN -> permissionStyle = config.lockScreenConfig
        }
        recordCount(context, bo)
        if(bo == PermissionBo.FLOATING_WINDOW){
            //申请悬浮窗样式
            ZsxTip(context).permissionStyle1Dialog(permissionStyle.image, permissionStyle.defaultResId, permissionStyle.title, permissionStyle.text, permissionStyle.btn){
                applyFloatingWindow(context)
            }
        }else{
            //申请后台界面弹出、自启动、锁屏显示样式
            ZsxTip(context).permissionStyle2Dialog(permissionStyle.image, permissionStyle.defaultResId, permissionStyle.title, permissionStyle.text, permissionStyle.btn){
                when(bo){
                    PermissionBo.BACKGROUND_POPUP -> applyBackgroundPopup(context)
                    PermissionBo.AUTO_RUN -> applyAutoRun(context, ApplyStep.FIRST_APPLY.value)
                    PermissionBo.LOCK_SCREEN -> applyLockScreen(context)
                }
            }
        }
    }

    private fun getSharedPreferences(context: Context): SharedPreferences{
        return context.getSharedPreferences("SpeedTest", Context.MODE_PRIVATE)
    }
}