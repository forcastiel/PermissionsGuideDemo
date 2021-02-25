package com.auntec.permissionsguide.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.ColorRes

/**
 * Context的扩展
 * @author shrek
 * @date:  2016-05-27
 */
fun Context.getResColor(@ColorRes colorVal:Int):Int {
    return getResources().getColor(colorVal)
}

//fun Context.getDimens(@ColorRes colorVal:Int):Int {
//    return getResources().getDimension(colorVal)
//}

fun Context.toastLongShow(info: String) {
    Toast.makeText(this, info, Toast.LENGTH_LONG).show()
}

fun Context.toastShortShow(info: String) {
    Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
}

fun Context.appVersion(): Int {
    val manager = this.packageManager
    val info = manager.getPackageInfo(this.packageName, 0)
    return info.versionCode
}

fun Context.appVersionName(): String {
    val manager = this.packageManager
    val info = manager.getPackageInfo(this.packageName, 0)
    return info.versionName
}

fun Context.targetSdkVersion(): Int {
    val manager = this.packageManager
    val info = manager.getPackageInfo(this.packageName, 0)
    return info.applicationInfo.targetSdkVersion
}




