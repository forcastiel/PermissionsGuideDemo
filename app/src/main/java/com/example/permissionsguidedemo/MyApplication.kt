package com.example.permissionsguidedemo

import androidx.multidex.MultiDexApplication
import com.auntec.permissionsguide.PermissionGuideAbility

class MyApplication : MultiDexApplication(), PermissionGuideAbility {

    override fun onCreate() {
        super.onCreate()
        initPermissionGuide()
    }

    private fun initPermissionGuide() {
        //module屏幕适配所需，得先执行
        permissionGuideDao.init(this)
        //初始化开关数据，开关默认开启，已设置默认样式
        permissionGuideDao.initData()
    }

}