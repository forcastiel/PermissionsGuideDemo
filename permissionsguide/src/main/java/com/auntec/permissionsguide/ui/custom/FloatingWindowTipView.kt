package com.auntec.permissionsguide.ui.custom

import android.content.Context
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.CustomTSDimens
import com.auntec.permissionsguide.extension.relateTextSize
import com.auntec.permissionsguide.ui.adaptation.pixelPadding
import org.jetbrains.anko.*

class FloatingWindowTipView(context: Context): _LinearLayout(context) {

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
            buildFloatingWindowTip().invoke(this)
        }.lparams(matchParent, wrapContent){}
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
}