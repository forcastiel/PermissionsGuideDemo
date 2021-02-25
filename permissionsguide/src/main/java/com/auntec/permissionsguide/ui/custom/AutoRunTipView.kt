package com.auntec.permissionsguide.ui.custom

import android.content.Context
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.CustomTSDimens
import com.auntec.permissionsguide.extension.relateTextSize
import com.auntec.permissionsguide.ui.adaptation.pixelPadding
import org.jetbrains.anko.*

class AutoRunTipView(context: Context): _LinearLayout(context) {

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
            buildAutoRunTip().invoke(this)
        }.lparams(matchParent, wrapContent){}
    }

    fun buildAutoRunTip(): _LinearLayout.() -> Unit{
        return {
            verticalLayout {
                textView("找到【自启动】点击打开") {
                    relateTextSize = CustomTSDimens.SIZE_1F
                    textColorResource = R.color.text_color
                }.lparams(matchParent, wrapContent){
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

}