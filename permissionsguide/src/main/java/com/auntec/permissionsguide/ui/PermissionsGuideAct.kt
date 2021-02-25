package com.auntec.permissionsguide.ui

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import com.auntec.permissionsguide.bo.PermissionBo
import com.auntec.permissionsguide.PermissionGuideAbility
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.bo.Const
import com.auntec.permissionsguide.extension.*
import com.auntec.permissionsguide.ui.adaptation.pixelPadding
import org.jetbrains.anko.*

class PermissionsGuideAct: BaseNavAct("操作指引"), PermissionGuideAbility {

    companion object {
        //申请自启动权限时，流程中两次跳转该页面，用以区分
        val REQUEST_CODE1 = 1001
        val REQUEST_CODE2 = 1002
    }

    override fun initContent(): _LinearLayout.() -> Unit {
        var permissionBo = intent.getSerializableExtra(Const.BO) as PermissionBo
        return {
            verticalLayout {
                backgroundColor = Color.parseColor("#13141D")
                textView("即将前往系统页面") {
                    relateTextSize = CustomTSDimens.SIZE_16F
                    textColorResource = R.color.text_color
                }.lparams(){
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dip(70)
                }
                textView("请完成以下操作") {
                    relateTextSize = CustomTSDimens.SIZE_3F
                    textColorResource = R.color.text_color
                }.lparams(){
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dip(16)
                }

                verticalLayout {
                    when(permissionBo){
                        PermissionBo.BACKGROUND_POPUP -> buildBackgroundPopupTip().invoke(this)
                        PermissionBo.AUTO_RUN -> buildAutoRunTip().invoke(this)
                        PermissionBo.LOCK_SCREEN -> buildLockScreenTip().invoke(this)
                    }
                }.lparams(matchParent, wrapContent){
                    horizontalMargin = dip(50)
                    topMargin = dip(30)
                    bottomMargin = if(permissionBo == PermissionBo.AUTO_RUN) dip(150) else dip(62)
                }


                stateButton {
                    kRandomId()
                    text = "立即前往"
                    gravity = Gravity.CENTER
                    setRoundRadius()
                    setNormalBackgroundColor(getResColor(R.color.color_blue))
                    setPressedBackgroundColor(getResColor(R.color.color_blue))
                    setNormalTextColor(Color.WHITE)
                    setPressedTextColor(Color.WHITE)
                    setOnClickListener {
                        when(permissionBo){
                            PermissionBo.BACKGROUND_POPUP -> permissionGuideDao.applyBackgroundPopup(this@PermissionsGuideAct)
//                            PermissionBo.AUTO_RUN -> {
//                                var isTipShowing = false
//                                AutoRunUtils.requestPermission(act as Activity, object :
//                                    OnPermissionResult {
//                                    override fun permissionResult(isOpen: Boolean) {
//                                        //三星机型上该回调会执行两次
//                                        if(isTipShowing){ return }
//                                        isTipShowing = true
//                                        ZsxTip(this@PermissionsGuideAct).chooseDialog("是否已开启权限？", "只有当你成功完成设置，才可以提升网络测速诊断服务", "已开启权限", "未开启权限"){
//                                            isTipShowing = false
//                                            if(it){
//                                                ZsxTip(this@PermissionsGuideAct).permissionStyle2Dialog("", R.drawable.ic_tishi_fail, "部分能力开启失败", "再次开启，享受顶级测速诊断能力", "立即开启"){
//                                                    startActivity<PermissionsGuideAct>(Const.BO to PermissionBo.AUTO_RUN)
//                                                }
//                                            }else{
//                                                //本地记录自启动权限已开启
//                                                permissionGuideDao.recordPermissionState(act, true)
//                                            }
//                                        }
//                                    }
//                                })
//                            }
                            PermissionBo.AUTO_RUN -> {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                            PermissionBo.LOCK_SCREEN -> permissionGuideDao.applyLockScreen(this@PermissionsGuideAct)
                        }
                    }
                }.lparams(matchParent, dip(44)) {
                    horizontalMargin = dip(70)
                }

            }.lparams(matchParent, matchParent){}
        }
    }

    fun buildBackgroundPopupTip(): _LinearLayout.() -> Unit{
        return {
            verticalLayout {
                textView("1. 下滑找到【后台弹出界面】并点击") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams{
                    bottomMargin = dip(12)
                }

                relativeLayout {
                    backgroundColorResource = R.color.text_color
                    var label= textView("后台弹出界面") {
                        kRandomId()
                        relateTextSize = CustomTSDimens.SIZE_1F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams{
                        leftMargin = dip(12)
                        topMargin = dip(12)
                        bottomMargin = dip(6)
                    }
                    textView("允许应用在后台弹出界面") {
                        relateTextSize = CustomTSDimens.SIZE_0F
                        textColorResource = R.color.colorTxtGray
                    }.lparams{
                        below(label)
                        leftMargin = dip(12)
                        bottomMargin = dip(12)
                    }
                    imageView{
                        imageResource = R.drawable.icon_close
                    }.lparams(dip(16), dip(16)){
                        alignParentRight()
                        centerVertically()
                        rightMargin = dip(12)
                    }
                }.lparams(matchParent, wrapContent){
                    bottomMargin = dip(20)
                }

                textView("2. 选择【始终允许】") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams{
                    bottomMargin = dip(12)
                }

                relativeLayout {
                    verticalLayout {
                        backgroundColorResource = R.color.text_color
                        textView("拒绝") {
                            pixelPadding(dip(12), 0, 0, 0)
                            gravity = Gravity.CENTER_VERTICAL
                            relateTextSize = CustomTSDimens.SIZE_0F
                            textColorResource = R.color.colorTxtDarkGray
                        }.lparams(matchParent, dip(37)){
                            topMargin = dip(8)
                        }
                        relativeLayout {
                            backgroundColorResource = R.color.colorLightBlue
                            textView("始终允许") {
                                relateTextSize = CustomTSDimens.SIZE_1F
                                textColorResource = R.color.colorBlue
                                gravity = Gravity.CENTER_VERTICAL
                            }.lparams(matchParent, dip(37)){
                                centerVertically()
                                leftMargin = dip(12)
                            }
                            imageView{
                                backgroundResource = R.drawable.icon_tip_select
                            }.lparams(dip(16), dip(16)){
                                alignParentRight()
                                centerVertically()
                                rightMargin = dip(12)
                            }
                        }.lparams(matchParent, dip(37)){
                            bottomMargin = dip(8)
                        }
                    }.lparams(matchParent, dip(89)){}

                    imageView{
                        imageResource = R.drawable.icon_finger
                    }.lparams(dip(23), dip(28)){
                        alignParentBottom()
                        leftMargin = dip(69)
                    }
                }.lparams(matchParent, dip(110)){
                    bottomMargin = dip(20)
                }

            }.lparams(matchParent, wrapContent){}
        }
    }

    fun buildAutoRunTip(): _LinearLayout.() -> Unit{
        return {
            verticalLayout {
                textView("找到【自启动】点击打开") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams{
                    bottomMargin = dip(20)
                }

                relativeLayout {
                    backgroundColorResource = R.color.text_color
                    textView("自启动") {
                        relateTextSize = CustomTSDimens.SIZE_1F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams{
                        leftMargin = dip(12)
                        centerVertically()
                    }
                    imageView{
                        imageResource = R.drawable.icon_kaiguan
                    }.lparams(dip(31), dip(17)){
                        rightMargin = dip(10)
                        centerVertically()
                        alignParentRight()
                    }
                    imageView{
                        imageResource = R.drawable.icon_finger
                    }.lparams(dip(23), dip(28)){
                        alignParentRight()
                        alignParentBottom()
                        rightMargin = dip(10)
                        bottomMargin = dip(2)
                    }
                }.lparams(matchParent, dip(60)){
                    bottomMargin = dip(30)
                }
            }.lparams(matchParent, wrapContent){}
        }
    }

    fun buildLockScreenTip(): _LinearLayout.() -> Unit{
        return {
            verticalLayout {
                textView("1. 下滑找到【锁屏显示】并点击") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams{
                    bottomMargin = dip(12)
                }

                relativeLayout {
                    backgroundColorResource = R.color.text_color
                    var label= textView("锁屏显示") {
                        kRandomId()
                        relateTextSize = CustomTSDimens.SIZE_1F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams{
                        leftMargin = dip(12)
                        topMargin = dip(12)
                        bottomMargin = dip(6)
                    }
                    textView("允许应用在锁屏上显示") {
                        relateTextSize = CustomTSDimens.SIZE_0F
                        textColorResource = R.color.colorTxtGray
                    }.lparams{
                        below(label)
                        leftMargin = dip(12)
                        bottomMargin = dip(12)
                    }
                    imageView{
                        imageResource = R.drawable.icon_close
                    }.lparams(dip(16), dip(16)){
                        alignParentRight()
                        centerVertically()
                        rightMargin = dip(12)
                    }
                }.lparams(matchParent, wrapContent){
                    bottomMargin = dip(20)
                }

                textView("2. 选择【始终允许】") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams{
                    bottomMargin = dip(12)
                }

                relativeLayout {
                    verticalLayout {
                        backgroundColorResource = R.color.text_color
                        textView("拒绝") {
                            pixelPadding(dip(12), 0, 0, 0)
                            gravity = Gravity.CENTER_VERTICAL
                            relateTextSize = CustomTSDimens.SIZE_0F
                            textColorResource = R.color.colorTxtDarkGray
                        }.lparams(matchParent, dip(37)){
                            topMargin = dip(8)
                        }
                        relativeLayout {
                            backgroundColorResource = R.color.colorLightBlue
                            textView("始终允许") {
                                relateTextSize = CustomTSDimens.SIZE_1F
                                textColorResource = R.color.colorBlue
                                gravity = Gravity.CENTER_VERTICAL
                            }.lparams(matchParent, dip(37)){
                                centerVertically()
                                leftMargin = dip(12)
                            }
                            imageView{
                                backgroundResource = R.drawable.icon_tip_select
                            }.lparams(dip(16), dip(16)){
                                alignParentRight()
                                centerVertically()
                                rightMargin = dip(12)
                            }
                        }.lparams(matchParent, dip(37)){
                            bottomMargin = dip(8)
                        }
                    }.lparams(matchParent, dip(89)){}

                    imageView{
                        imageResource = R.drawable.icon_finger
                    }.lparams(dip(23), dip(28)){
                        alignParentBottom()
                        leftMargin = dip(69)
                    }
                }.lparams(matchParent, dip(110)){
                    bottomMargin = dip(20)
                }

            }.lparams(matchParent, wrapContent){}
        }
    }
}