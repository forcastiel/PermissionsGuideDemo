package com.auntec.permissionsguide.ui.custom

import android.content.Context
import android.view.Gravity
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.CustomTSDimens
import com.auntec.permissionsguide.extension.kRandomId
import com.auntec.permissionsguide.extension.relateTextSize
import com.auntec.permissionsguide.ui.adaptation.pixelPadding
import org.jetbrains.anko.*

class BackgroundPopupTipView(context: Context): _LinearLayout(context) {

    init {
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
            }.lparams(matchParent, wrapContent){
                bottomMargin = dip(22)
            }
            buildBackgroundPopupTip().invoke(this)
        }.lparams(matchParent, wrapContent){}
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

}