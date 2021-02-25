package com.auntec.permissionsguide.uitls.floatingwindow

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.auntec.permissionsguide.extension.uiThread
import com.auntec.permissionsguide.uitls.autorun.AutoRunUtils
import com.auntec.permissionsguide.uitls.backgroundstart.BackgroundStartUtils
import com.auntec.permissionsguide.uitls.lockscreen.LockScreenUtils

/**
 * @author: liuzhenfeng
 * @function: 用于浮窗权限的申请，自动处理回调结果
 * @date: 2019-07-15  10:36
 */
internal class PermissionFragment : Fragment() {

    companion object {
        var permissionType = 0 //0:悬浮窗; 1:后台弹出界面; 2:自启动; 3:锁屏显示
        private var onPermissionResult: OnPermissionResult? = null

        fun requestPermission(activity: Activity, permissionType: Int, onPermissionResult: OnPermissionResult) {
            Companion.permissionType = permissionType
            Companion.onPermissionResult = onPermissionResult
            activity.fragmentManager
                .beginTransaction()
                .add(PermissionFragment(), activity.localClassName)
                .commitAllowingStateLoss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 权限申请
        if(permissionType == 0){
            FloatingWindowUtils.requestPermission(this)
        }else if(permissionType == 1){
            BackgroundStartUtils.requestPermission(this)
        }else if(permissionType == 2){
            AutoRunUtils.requestPermission(this)
        }else if(permissionType == 3){
            LockScreenUtils.requestPermission(this)
        }
        Log.i("PermissionFragment","PermissionFragment：requestPermission")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        uiThread(500) {
            if (requestCode == FloatingWindowUtils.requestCode) {
                callBack(FloatingWindowUtils.checkPermission(activity))
            }else if(requestCode == BackgroundStartUtils.requestCode){
                callBack(BackgroundStartUtils.checkPermission(activity))
            }else if(requestCode == AutoRunUtils.requestCode){
                callBack(false)
            }else if(requestCode == LockScreenUtils.requestCode){
                callBack(LockScreenUtils.checkPermission(activity))
            }
        }
    }

    fun callBack(result: Boolean){
        // 需要延迟执行，不然即使授权，仍有部分机型获取不到权限
//        Handler(Looper.getMainLooper()).postDelayed({
//            val activity = activity ?: return@postDelayed
            Log.i("PermissionFragment", "PermissionFragment onActivityResult: $result")
            // 回调权限结果
            onPermissionResult?.permissionResult(result)
            // 将Fragment移除
            //这边注释掉是因为在oppo和三星机型上，跳转自启动系统页面，该fragment会直接destroy导致回调不执行
//            fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
//        }, 500)
    }

}