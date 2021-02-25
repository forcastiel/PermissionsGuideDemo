package com.example.permissionsguidedemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.auntec.permissionsguide.PermissionGuideAbility
import com.auntec.permissionsguide.bo.ApplyStep
import com.auntec.permissionsguide.bo.PermissionBo
import com.auntec.permissionsguide.extension.toastLongShow
import com.auntec.permissionsguide.ui.CheckNetworkAct
import com.auntec.permissionsguide.ui.PermissionsGuideAct
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), PermissionGuideAbility {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        Log.e("ttt", "悬浮窗权限是否开启===>${permissionGuideDao.isFloatingWindowOpen(this)}")
        Log.e("ttt", "后台弹出界面权限是否开启===>${permissionGuideDao.isBackgroundPopupOpen(this)}")
        Log.e("ttt", "自启动权限是否开启===>${permissionGuideDao.isAutoRunOpen(this)}")
        Log.e("ttt", "锁屏显示权限是否开启===>${permissionGuideDao.isLockScreenOpen(this)}")
        //自动选择权限并展示相应弹窗
        button1.setOnClickListener {
            var permissionBo = permissionGuideDao.getPermissionToShow(this)
            if(permissionBo != PermissionBo.NULL){
                permissionGuideDao.showApplyPermissionTip(this, permissionBo)
            }else{
                toastLongShow("今日已不需要再弹出权限申请框")
            }
        }

        //跳转系统悬浮窗管理页
        button2.setOnClickListener {
            permissionGuideDao.applyFloatingWindow(this, true)
        }

        //跳转系统后台弹出界面管理页
        button3.setOnClickListener {
            permissionGuideDao.applyBackgroundPopup(this, true)
        }

        //跳转系统自启动管理页, step 0:第一步, 1:第二步, 2:第三步
        button4.setOnClickListener {
            permissionGuideDao.applyAutoRun(this, ApplyStep.FIRST_APPLY.value)
        }

        //跳转系统锁屏显示管理页
        button5.setOnClickListener {
            permissionGuideDao.applyLockScreen(this, true)
        }

        //网络检测宝典
        button6.setOnClickListener {
            startActivity<CheckNetworkAct>()
        }

    }

    //用于处理跳转自启动系统页返回
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PermissionsGuideAct.REQUEST_CODE1){
                permissionGuideDao.applyAutoRun(this, ApplyStep.SECOND_APPLY.value)
            }else if(requestCode == PermissionsGuideAct.REQUEST_CODE2){
                permissionGuideDao.applyAutoRun(this, ApplyStep.THIRD_APPLY.value)
            }
        }
    }
}