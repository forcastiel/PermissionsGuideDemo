package com.auntec.permissionsguide.bo

import com.auntec.permissionsguide.R
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


enum class PermissionBo(val title: String, val descript: String, val priority: Int, val resId: Int, val label: String){
    FLOATING_WINDOW("深度测速", "需开启悬浮窗权限", 4, R.drawable.icon_xuanfuchuang, "FLOATING_WINDOW"),
    BACKGROUND_POPUP("网络智能安全系统","需后台弹出界面权限", 3, R.drawable.icon_background_popup, "BACKGROUND_POPUP"),
    AUTO_RUN("WiFi安全感知系统","需开启自启动权限", 2, R.drawable.icon_ziqidong, "AUTO_RUN"),
    LOCK_SCREEN("WiFi节能大师","需锁屏显示权限", 1, R.drawable.icon_suoping, "LOCK_SCREEN"),
    READ_PHONE_STATE("本机识别码","启动服务的必要条件", 5, R.drawable.icon_phone, "READ_PHONE_STATE"),
    WRITE_EXTERNAL_STORAGE("文件识别","读写设备上的文件", 6, R.drawable.icon_duxie, "WRITE_EXTERNAL_STORAGE"),
    NULL("", "", 0, R.drawable.icon_xuanfuchuang, "NULL")
}

fun PermissionBo.key(): String{
    return "${this.label}_${SimpleDateFormat("yyyy_MM_dd").format(Date())}"
}

class PermissionGuideConfig(): Serializable {
    var applyFloatingWindow = false  //权限引导，是否需要申请悬浮窗权限
    var applyBackgroundPopup = false  //权限引导，是否需要申请后台弹出界面权限
    var applyAutoRun = false  //权限引导，是否需要申请自启动权限
    var applyLockScreen = false  //权限引导，是否需要申请锁屏显示权限

    var floatingWindowConfig = PermissionStyle()  //权限引导，申请悬浮窗样式
    var backgroundPopupConfig = PermissionStyle()  //权限引导，申请后台弹出界面样式
    var autoRunConfig = PermissionStyle()  //权限引导，申请自启动样式
    var lockScreenConfig = PermissionStyle()  //权限引导，申请锁屏显示样式
}

class PermissionStyle(): Serializable{
    var limit = 1
    var image = ""
    var title = ""
    var text = ""
    var btn = "立即开启"
    var defaultResId = R.drawable.ic_xuanfuchuang

    constructor(limit: Int, image: String, title: String, text: String, btn: String, defaultResId: Int) : this() {
        this.limit = limit
        this.image = image
        this.title = title
        this.text = text
        this.btn = btn
        this.defaultResId = defaultResId
    }



    override fun toString(): String {
        return "PermissionStyle(limit=$limit, image='$image', title='$title', text='$text', btn='$btn', defaultResId=$defaultResId)"
    }
}

enum class ApplyStep(val value: Int, val desc: String){
    FIRST_APPLY(0, "申请权限第一步"),
    SECOND_APPLY(1, "申请权限第二步"),
    THIRD_APPLY(2, "申请权限第三步")
}