package com.auntec.permissionsguide.uitls.backgroundstart

import android.app.Activity
import android.app.AppOpsManager
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.auntec.permissionsguide.uitls.floatingwindow.OnPermissionResult
import com.auntec.permissionsguide.uitls.floatingwindow.PermissionFragment
import com.auntec.permissionsguide.uitls.floatingwindow.rom.RomUtils
import java.lang.reflect.Method


object BackgroundStartUtils {

    internal const val requestCode = 200

    @JvmStatic
    fun checkPermission(context: Context): Boolean =
        when {
            RomUtils.checkIsMiuiRom() -> canBackgroundStartForMIUI(context)
            RomUtils.checkIsVivoRom() -> canBackgroundStartForVIVO(context)
            else -> true
        }

    /**
     * 申请后台弹出界面权限
     */
    @JvmStatic
    fun requestPermission(activity: Activity, onPermissionResult: OnPermissionResult) =
        PermissionFragment.requestPermission(activity, 1, onPermissionResult)

    internal fun requestPermission(fragment: Fragment) =
        if(RomUtils.checkIsMiuiRom()){
            applyPermissionForXIAOMI(fragment)
        }else{
            //VIVO
            applyPermissionForVIVO(fragment)
        }


    /**
     * 小米判断是否有后台弹出界面权限
     */
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun canBackgroundStartForMIUI(context: Context): Boolean {
        val ops =
            context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        try {
            val op = 10021 // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            val method: Method = ops.javaClass.getMethod(
                "checkOpNoThrow", *arrayOf<Class<*>?>(
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
            )
            val result = method.invoke(ops, op, android.os.Process.myUid(), context.packageName) as Int
            return result == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 小米跳转后台弹出界面权限管理页
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
     * 判断vivo后台弹出界面 1未开启 0开启
     * @param context
     * @return
     */
    fun canBackgroundStartForVIVO(context: Context): Boolean {
        val packageName = context.packageName
        val uri2: Uri = Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity")
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
     * VIVO跳转后台弹出界面权限管理页
     */
    fun applyPermissionForVIVO(fragment: Fragment){
        try{
            val localIntent: Intent
            if (Build.MODEL.contains("Y85") && !Build.MODEL.contains("Y85A") || Build.MODEL.contains("vivo Y53L")) {
                localIntent = Intent()
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity")
                localIntent.putExtra("packagename", fragment.activity.packageName)
                localIntent.putExtra("tabId", "1")
                fragment.startActivityForResult(localIntent, requestCode)
            } else {
                localIntent = Intent()
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
                localIntent.action = "secure.intent.action.softPermissionDetail"
                localIntent.putExtra("packagename", fragment.activity.packageName)
                fragment.startActivityForResult(localIntent, requestCode)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }





}