package com.auntec.permissionsguide.uitls.floatingwindow.rom

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.util.Log
import android.widget.Toast

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
object OppoUtils {
    private const val TAG = "OppoUtils"

    /**
     * 检测 360 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else true
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz: Class<*> = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod(
                    "checkOp",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
                return AppOpsManager.MODE_ALLOWED == method.invoke(
                    manager,
                    op,
                    Binder.getCallingUid(),
                    context.packageName
                ) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!")
        }
        return false
    }

    /**
     * oppo ROM 权限申请
     */
    fun applyOppoPermission(context: Context) {
        try {
            val intent = Intent()
            intent.setClassName("com.oppo.safe", "com.oppo.safe.SecureSafeMainActivity")
            context.startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
            try {
                val intent = Intent("action.coloros.safecenter.FloatWindowListActivity")
                intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity")
                context.startActivity(intent)
            } catch (ee: Exception) {
                ee.printStackTrace()
                try {
                    val i = Intent("com.coloros.safecenter")
                    i.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity")
                    context.startActivity(i)
                } catch (eee: Exception) {
                    eee.printStackTrace()
                    try {
                        val i = Intent()
                        i.component = ComponentName("com.color.safecenter", "com.color.safecenter.SecureSafeMainActivity")
                        context.startActivity(i)
                    } catch (es: Exception) {
                        Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}