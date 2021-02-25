package com.auntec.permissionsguide.uitls.lockscreen

import android.app.Activity
import android.app.AppOpsManager
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import com.auntec.permissionsguide.uitls.floatingwindow.OnPermissionResult
import com.auntec.permissionsguide.uitls.floatingwindow.PermissionFragment
import com.auntec.permissionsguide.uitls.floatingwindow.rom.RomUtils
import java.lang.reflect.Method


object LockScreenUtils {

    internal const val requestCode = 202

    @JvmStatic
    fun checkPermission(context: Context): Boolean =
        if(RomUtils.checkIsMiuiRom()){
            canLockScreenForXIAOMI(context)
        }else if(RomUtils.checkIsVivoRom()){
            canLockScreenForVIVO(context)
        }else{
            true
        }

    /**
     * 申请后台弹出界面权限
     */
    @JvmStatic
    fun requestPermission(activity: Activity, onPermissionResult: OnPermissionResult) =
        PermissionFragment.requestPermission(activity, 3, onPermissionResult)

    internal fun requestPermission(fragment: Fragment) =
        if(RomUtils.checkIsMiuiRom()){
            //已验证 小米8
            applyPermissionForXIAOMI(fragment)
        }else if(RomUtils.checkIsVivoRom()){
            //已验证 vivo X9s、vivo Y93s、vivo X20A
            applyPermissionForVIVO(fragment)
        }else{

        }

    fun canLockScreenForXIAOMI(context: Context): Boolean {
        var ops: AppOpsManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ops = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        }
        try {
            val op = 10020 // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            val method: Method = ops!!.javaClass.getMethod(
                "checkOpNoThrow", *arrayOf<Class<*>?>(
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
            )
            return method.invoke(ops, op, Process.myUid(), context.packageName) == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun canLockScreenForVIVO(context: Context): Boolean {
        val packageName = context.packageName
        val uri2: Uri = Uri.parse("content://com.vivo.permissionmanager.provider.permission/control_locked_screen_action")
        val selection = "pkgname = ?"
        val selectionArgs = arrayOf(packageName)
        try {
            val cursor: Cursor? = context
                .contentResolver
                .query(uri2, null, selection, selectionArgs, null)
            if (cursor != null) {
                return if (cursor.moveToFirst()) {
                    val currentmode: Int = cursor.getInt(cursor.getColumnIndex("currentstate"))
                    cursor.close()
                    currentmode == 0
                } else {
                    cursor.close()
                    false
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
        return false
    }

    /**
     * 小米
     */
    fun applyPermissionForXIAOMI(fragment: Fragment){
        try{
            val xiaomiBackGroundIntent = Intent()
            xiaomiBackGroundIntent.action = "miui.intent.action.APP_PERM_EDITOR"
            xiaomiBackGroundIntent.addCategory(Intent.CATEGORY_DEFAULT)
            xiaomiBackGroundIntent.putExtra("extra_pkgname", fragment.activity.packageName)
            fragment.startActivityForResult(xiaomiBackGroundIntent, requestCode)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    /**
     * VIVO
     */
    fun applyPermissionForVIVO(fragment: Fragment){
        var localIntent: Intent
        try {
            if (Build.MODEL.contains("Y85") && !Build.MODEL.contains("Y85A") || Build.MODEL.contains(
                    "vivo Y53L"
                )
            ) {
                localIntent = Intent()
                localIntent.setClassName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.PurviewTabActivity"
                )
                localIntent.putExtra("packagename", fragment.activity.packageName)
                localIntent.putExtra("tabId", "1")
                fragment.startActivityForResult(localIntent, requestCode)
            } else {
                localIntent = Intent()
                localIntent.setClassName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"
                )
                localIntent.action = "secure.intent.action.softPermissionDetail"
                localIntent.putExtra("packagename", fragment.activity.packageName)
                fragment.startActivityForResult(localIntent, requestCode)
            }
        } catch (e: java.lang.Exception) {
            // 否则跳转到应用详情
            localIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri =
                Uri.fromParts("package", fragment.activity.packageName, null)
            localIntent.data = uri
            fragment.startActivityForResult(localIntent, requestCode)
        }
    }

}