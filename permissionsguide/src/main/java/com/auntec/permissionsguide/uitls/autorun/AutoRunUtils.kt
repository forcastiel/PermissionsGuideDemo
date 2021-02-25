package com.auntec.permissionsguide.uitls.autorun

import android.app.Activity
import android.app.Fragment
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.auntec.permissionsguide.uitls.floatingwindow.OnPermissionResult
import com.auntec.permissionsguide.uitls.floatingwindow.PermissionFragment
import com.auntec.permissionsguide.uitls.floatingwindow.rom.RomUtils


object AutoRunUtils {

    internal const val requestCode = 201

    @JvmStatic
    fun checkPermission(context: Context): Boolean = false

    /**
     * 申请后台弹出界面权限
     */
    @JvmStatic
    fun requestPermission(activity: Activity, onPermissionResult: OnPermissionResult) =
        PermissionFragment.requestPermission(activity, 2, onPermissionResult)

    internal fun requestPermission(fragment: Fragment) =
        if(RomUtils.checkIsHuaweiRom()){
            applyPermissionForHUAWEI(fragment)
        }else if(RomUtils.checkIsMiuiRom()){
            applyPermissionForXIAOMI(fragment)
        }else if(RomUtils.checkIsSamSungRom()){
            applyPermissionForSAMSUNG(fragment)
        }else if(RomUtils.checkIsMeizuRom()){
            applyPermissionForMEIZU(fragment)
        }else if(RomUtils.checkIsVivoRom()){
            applyPermissionForVIVO(fragment)
        }else if(RomUtils.checkIsOppoRom()){
            applyPermissionForOPPO(fragment)
        }else{
            jumpAPPInfo(fragment)
        }

    /**
     *
     */
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun canAutoRun(context: Context): Boolean {
        return false
    }

    /**
     * 华为
     */
    fun applyPermissionForHUAWEI(fragment: Fragment){
        var componentName: ComponentName? = null
        val sdkVersion = Build.VERSION.SDK_INT
        try {
            val intent = Intent()
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //跳自启动管理
            if (sdkVersion >= 28) { //9:已测试
                componentName =
                    ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity") //跳自启动管理
            } else if (sdkVersion >= 26) { //8：已测试
                componentName =
                    ComponentName.unflattenFromString("com.huawei.systemmanager/.appcontrol.activity.StartupAppControlActivity")
            } else if (sdkVersion >= 23) { //7.6：已测试
                componentName =
                    ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity")
            } else if (sdkVersion >= 21) { //5
                componentName =
                    ComponentName.unflattenFromString("com.huawei.systemmanager/com.huawei.permissionmanager.ui.MainActivity")
            }
            //componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");//锁屏清理
            intent.component = componentName
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: Exception) {
            //跳转失败
            jumpAPPInfo(fragment)
        }
    }

    /**
     * 小米
     */
    fun applyPermissionForXIAOMI(fragment: Fragment){
//        val intent = Intent()
//        try {
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            var componentName: ComponentName? = null
//            componentName = ComponentName(
//                "com.miui.securitycenter",
//                "com.miui.permcenter.autostart.AutoStartManagementActivity"
//            )
//            intent.component = componentName
//            fragment.startActivityForResult(intent, requestCode)
//        } catch (e: java.lang.Exception) { //抛出异常就直接打开设置页面
//            jumpAPPInfo(fragment)
//        }

        try{
            jumpAPPInfo(fragment)
        }catch (e: Exception){
            Log.e("ttt", "====>${e.message}")
            val intent = Intent()
            try {
                var componentName: ComponentName? = null
                componentName = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
                intent.component = componentName
                fragment.startActivityForResult(intent, requestCode)
            } catch (e: java.lang.Exception) { //抛出异常就直接打开设置页面
            }
        }
    }

    /**
     * 三星
     */
    fun applyPermissionForSAMSUNG(fragment: Fragment){
        val intent = Intent()
        var componentName: ComponentName? = null
        try {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //跳自启动管理
            componentName = ComponentName.unflattenFromString("com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity")
            intent.component = componentName
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: java.lang.Exception) {
            try {
//                componentName = ComponentName(
//                    "com.samsung.android.sm_cn",
//                    "com.samsung.android.sm.ui.ram.AutoRunActivity"
//                )

                //自启动应用列表页，直接打开该页面会权限被拒
                //                componentName = ComponentName.unflattenFromString("com.samsung.android.sm_cn/com.samsung.android.sm.ui.ram.AutoRunActivity")
                //跳转三星智能管理器App的自启动应用列表页前一个页面
                componentName = ComponentName.unflattenFromString("com.samsung.android.sm_cn/com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity")

                intent.component = componentName
                fragment.startActivityForResult(intent, requestCode)
            } catch (e1: java.lang.Exception) {
                //跳转失败
                jumpAPPInfo(fragment)
            }
        }
    }

    /**
     * 魅族
     */
    fun applyPermissionForMEIZU(fragment: Fragment){
        val intent = Intent()
        var componentName: ComponentName? = null
        try {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //跳自启动管理
            componentName =
                ComponentName.unflattenFromString("com.meizu.safe/.permission.SmartBGActivity") //跳转到后台管理页面
            intent.component = componentName
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: java.lang.Exception) {
            try {
                componentName =
                    ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity") //跳转到手机管家
                intent.component = componentName
                fragment.startActivityForResult(intent, requestCode)
            } catch (e1: java.lang.Exception) {
                //跳转失败
                jumpAPPInfo(fragment)
            }
        }
    }

    /**
     * VIVO
     */
    fun applyPermissionForVIVO(fragment: Fragment){
        val intent = Intent()
        var componentName: ComponentName? = null
        try {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //跳自启动管理，权限被拒
//            componentName = ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.BgStartUpManagerActivity")
            //跳权限管理页面成功
            componentName = ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.PurviewTabActivity")
            intent.component = componentName
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: java.lang.Exception) {
            Log.e("ttt", e.message.toString())
            e.printStackTrace()
            jumpAPPInfo(fragment)
        }

    }

    /**
     * OPPO
     */
    fun applyPermissionForOPPO(fragment: Fragment){
        var intent = Intent()
        try {
            intent.setClassName("com.oppo.safe", "com.oppo.safe.SecureSafeMainActivity")
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: Throwable) {
            try {
                intent.component = ComponentName.unflattenFromString("com.coloros.safecenter/.startupapp.StartupAppListActivity");
                fragment.startActivityForResult(intent, requestCode)
            } catch (eee: Exception) {
                eee.printStackTrace()
                try {
                    intent = Intent()
                    intent.component = ComponentName("com.color.safecenter", "com.color.safecenter.SecureSafeMainActivity")
                    fragment.startActivityForResult(intent, requestCode)
                } catch (es: Exception) {
                    //oppo R11s
                    // 否则跳转到应用详情
                    jumpAPPInfo(fragment)
                }
            }
        }
    }

    fun jumpAPPInfo(fragment: Fragment){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", fragment.activity.packageName, null)
        intent.data = uri
        fragment.startActivityForResult(intent, requestCode)
    }

}