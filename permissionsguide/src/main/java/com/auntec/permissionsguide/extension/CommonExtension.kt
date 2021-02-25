package com.auntec.permissionsguide.extension

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import java.util.*

fun <T> T.weakSelf(): WeakReference<T> {
    return WeakReference<T>(this)
}

/**
 * 随机做什么
 */
fun Any.random(init:(Random) -> Unit) {
    init(Random())
}

/**
 * 检测权限
 *
 * @return true：已授权； false：未授权；
 */
fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
