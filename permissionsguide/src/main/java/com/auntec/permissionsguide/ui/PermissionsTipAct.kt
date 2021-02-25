package com.auntec.permissionsguide.ui

import android.view.Gravity
import android.widget.LinearLayout
import com.auntec.permissionsguide.bo.PermissionBo
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.bo.Const
import com.auntec.permissionsguide.extension.CustomTSDimens
import com.auntec.permissionsguide.extension.kRandomId
import com.auntec.permissionsguide.extension.relateTextSize
import com.auntec.permissionsguide.ui.adaptation.pixelPadding
import org.jetbrains.anko.*

class PermissionsTipAct: BaseAct() {

    override fun initView() {
        var permissionBo = intent.getSerializableExtra(Const.BO) as PermissionBo

        relativeLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)
            verticalLayout {
                backgroundResource = R.drawable.shape_bg_blue_radius_4
                pixelPadding(dip(12), dip(12), dip(12), 0)
                relativeLayout {
                    textView("请完成以下操作"){
                        relateTextSize = CustomTSDimens.SIZE_1F
                        textColorResource = R.color.text_color
                    }.lparams(){
                        centerVertically()
                    }
                    relativeLayout {
                        imageView{
                            imageResource = R.drawable.icon_close_tip
                        }.lparams(dip(16), dip(16)){
                            alignParentRight()
                        }
                        setOnClickListener { finish() }
                    }.lparams(dip(32), dip(32)){
                        alignParentRight()
                        centerVertically()
                    }
                }.lparams(matchParent, wrapContent){
                    bottomMargin = dip(22)
                }

                when(permissionBo){
                    PermissionBo.FLOATING_WINDOW -> buildFloatingWindowTip().invoke(this)
                    PermissionBo.BACKGROUND_POPUP -> buildBackgroundPopupTip().invoke(this)
                    PermissionBo.AUTO_RUN -> buildAutoRunTip().invoke(this)
                    PermissionBo.LOCK_SCREEN -> buildLockScreenTip().invoke(this)
                }
            }.lparams(matchParent, wrapContent){
                horizontalMargin = dip(30)
                centerInParent()
            }
        }
    }

    fun buildFloatingWindowTip(): _LinearLayout.() -> Unit{
        return {
            verticalLayout {
                textView("打开【允许显示在其他应用的上层】") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams{
                    bottomMargin = dip(24)
                }

                relativeLayout {
                    relativeLayout {
                        backgroundColorResource = R.color.text_color
                        textView("允许显示在其他应用的上层") {
                            relateTextSize = CustomTSDimens.SIZE_1F
                            textColorResource = R.color.colorTxtBlack
                        }.lparams{
                            leftMargin = dip(10)
                            centerVertically()
                        }
                        imageView{
                            imageResource = R.drawable.icon_kaiguan
                        }.lparams(dip(31), dip(17)){
                            rightMargin = dip(10)
                            centerVertically()
                            alignParentRight()
                        }
                    }.lparams(matchParent, dip(40)){}

                    imageView{
                        imageResource = R.drawable.icon_finger
                    }.lparams(dip(23), dip(28)){
                        alignParentRight()
                        alignParentBottom()
                        rightMargin = dip(12)
                    }
                }.lparams(matchParent, dip(49)){
                    bottomMargin = dip(15)
                }

            }.lparams(matchParent, wrapContent){}
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
                }.lparams(matchParent, dip(97)){
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
                    bottomMargin = dip(12)
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
                }.lparams(matchParent, dip(97)){
                    bottomMargin = dip(20)
                }

            }.lparams(matchParent, wrapContent){}
        }
    }
}