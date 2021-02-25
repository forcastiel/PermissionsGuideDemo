package com.auntec.permissionsguide.ui.custom

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.*
import com.auntec.permissionsguide.ui.adaptation.pixelLinearParams
import com.auntec.permissionsguide.uitls.CornerTransform
import com.bumptech.glide.Glide
import org.jetbrains.anko.*

class ZsxTip(val context: Context)  {

    companion object {
        const val DialogContentTextId = 0x9878
    }

    fun dialog(
        customView: View,
        style: Int = R.style.DialogTheme,
        isCancelable: Boolean = true,
        width: Int? = null,
        height: Int? = null
    ): AlertDialog? {
        //判断宿主情况
        var isVaild = true
        (context as? Activity)?.also {
            isVaild =
                !it.isFinishing && (if (Build.VERSION.SDK_INT >= 17) !it.isDestroyed else true)
        }
        if (!isVaild) {
            return null
        }
        val dialog = AlertDialog.Builder(context, style).setCancelable(isCancelable).show()
        dialog.setContentView(customView)
        val window = dialog?.getWindow()
        window?.setGravity(Gravity.CENTER)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//        window?.setWindowAnimations(R.style.popupAnimation);
        val m = (context as Activity).windowManager
        val d = m.defaultDisplay  //为获取屏幕宽、高
        width?.also {
            val params = window?.attributes
            params?.width = it
            dialog?.window?.attributes = params
        }
        height?.also {
            val params = window?.attributes
            params?.height = it
            dialog?.window?.attributes = params
        }
        return dialog
    }

    fun dialog(
        verPadding: Int = kIntWidth(0.04f),
        horPadding: Int = kIntWidth(0.04f),
        isCancelable: Boolean = true,
        bgColor: Int = R.color.text_color,
        viewProcess: _RelativeLayout.(AlertDialog) -> Unit
    ): AlertDialog? {
        val dialog = dialog(View(context), R.style.DialogTheme, isCancelable, kIntWidth(0.8f))
        if (dialog == null) {
            return null
        }
        val customView = context.UI {
            cardView {
                radius = DimensAdapter.dip5
                preventCornerOverlap = true
                useCompatPadding = true
//                val paddingVal = kIntWidth(0.04f)
                setContentPadding(horPadding, verPadding, horPadding, verPadding)
                setCardBackgroundColor(context.getResColor(bgColor))
//                backgroundColor = Color.WHITE
                relativeLayout {
                    viewProcess.invoke(this, dialog)
                }
            }
        }.view
        dialog?.setContentView(customView)
        return dialog
    }

    fun chooseDialog(
        title: String,
        content: CharSequence,
        leftTipTxt: String,
        rightTipTxt: String,
        isBold: Boolean = false,
        isCancelable: Boolean = true,
        process: (Boolean) -> Unit = { }
    ): AlertDialog? {
        return dialog(0, 0, isCancelable) { dialog ->
            val titleView = textView(title) {
                kRandomId()
                textColor = Color.parseColor("#06C280")
                textSize = DimensAdapter.textSpSize(CustomTSDimens.SIZE_3F)
            }.lparams {
                topMargin = dip(18)
                centerHorizontally()
                horizontalMargin = kIntWidth(0.02f)
            }

            val contentView = textView(content) {
                kRandomId()
                textColor = Color.parseColor("#333333")
                relateTextSize = if (isBold) CustomTSDimens.SIZE_1F else CustomTSDimens.SIZE_2F
                paint.isFakeBoldText = isBold
            }.lparams {
                topMargin = dip(12)
                leftMargin = dip(24)
                rightMargin = dip(24)
                below(titleView)
            }


            val line = view {
                kRandomId()
                backgroundColorResource = R.color.colorLineGray
            }.lparams(matchParent, 2) {
                below(contentView)
                topMargin = dip(18)
            }

            linearLayout {
                textView(leftTipTxt) {
                    kRandomId()
                    visibility = if (leftTipTxt.isNotEmpty()) View.VISIBLE else View.GONE
                    gravity = Gravity.CENTER
                    padding = kIntWidth(0.01f)
                    textColor = Color.parseColor("#555555")
                    textSize = DimensAdapter.textSpSize(CustomTSDimens.SIZE_2F)
                    setOnClickListener {
                        process.invoke(false)
                        dialog.dismiss()
                    }
                }.pixelLinearParams(0, wrapContent, 1f) {
                    topMargin = dip(12)
                    bottomMargin = dip(12)
                }

                view {
                    kRandomId()
                    visibility = if (leftTipTxt.isNotEmpty()) View.VISIBLE else View.GONE
                    backgroundColorResource = R.color.colorLineGray
                }.pixelLinearParams(2, matchParent) {}

                textView(rightTipTxt) {
                    kRandomId()
                    gravity = Gravity.CENTER
                    padding = kIntWidth(0.01f)
                    textColor = Color.parseColor("#555555")
                    textSize = DimensAdapter.textSpSize(CustomTSDimens.SIZE_2F)
                    setOnClickListener {
                        process.invoke(true)
                        dialog.dismiss()
                    }
                }.pixelLinearParams(0, wrapContent, 1f) {
                    topMargin = dip(12)
                    bottomMargin = dip(12)
                }
            }.lparams(matchParent, wrapContent) {
                below(line)
            }
        }
    }

    //显示正常的提示弹出框
    fun noticeDialog(
        title: String,
        content: String,
        tipTxt: String,
        process: () -> Unit = { }
    ): AlertDialog? {
        return chooseDialog(title, content, "", tipTxt) {
            if (true) {
                process.invoke()
            }
        }
    }

    //申请权限弹框样式1-内容可配置
    fun permissionStyle1Dialog(imgUrl: String, defaultResId: Int, title: String, desc: String, btnName: String, process: () -> Unit): AlertDialog? {
        val transformation = CornerTransform(context, context.dip(8).toFloat())
        transformation.setExceptCorner(false, false, true, true)

        val permissionStyle1Dialog = AlertDialog.Builder(context, R.style.DialogTheme).show()
        val permissionStyle1View = context.UI {
            relativeLayout {
                layoutParams = relativeLP(matchParent, wrapContent)
                backgroundResource = R.drawable.shape_bg_white_radius_8
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    imageView{
                        if(imgUrl.isNotEmpty()){
                            Glide.with(this)
                                .load(imgUrl)
                                .error(defaultResId)
                                .skipMemoryCache(true)
                                .transform(transformation)
                                .into(this)
                        }else{
                            Glide.with(this)
                                .load(defaultResId)
                                .skipMemoryCache(true)
                                .transform(transformation)
                                .into(this)
                        }
                    }.lparams(matchParent, kIntWidth(0.833f)*170/300){}

                    textView(title) {
                        gravity = Gravity.CENTER
                        relateTextSize = CustomTSDimens.SIZE_4F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(10)
                        topMargin = dip(20)
                    }

                    textView(desc) {
                        gravity = Gravity.CENTER
                        relateTextSize = CustomTSDimens.SIZE_2F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(35)
                        topMargin = dip(12)
                    }

                    stateButton {
                        kRandomId()
                        text = btnName
                        gravity = Gravity.CENTER
                        setRadiusByApp()
                        setNormalBackgroundColor(context.getResColor(R.color.colorGreen))
                        setPressedBackgroundColor(context.getResColor(R.color.colorGreen))
                        setNormalTextColor(Color.WHITE)
                        setPressedTextColor(Color.WHITE)
                        setOnClickListener {
                            permissionStyle1Dialog.dismiss()
                            process.invoke()
                        }
                    }.lparams(matchParent, dip(38)) {
                        horizontalMargin = dip(60)
                        verticalMargin = dip(18)
                    }

                }.lparams(matchParent, wrapContent){}

                relativeLayout {
                    imageView{
                        imageResource = R.drawable.icon_close_white
                    }.lparams(dip(16), dip(16)){
                        alignParentRight()
                    }

                    setOnClickListener {
                        permissionStyle1Dialog.dismiss()
                    }
                }.lparams(dip(32), dip(32)){
                    alignParentRight()
                    topMargin = dip(10)
                    rightMargin = dip(10)
                }

            }
        }.view

        //判断宿主情况
        var isVaild = true
        (context as? Activity)?.also {
            isVaild = !it.isFinishing && (if (Build.VERSION.SDK_INT >= 17) !it.isDestroyed else true)
        }
        if (!isVaild) {
            return null
        }

        permissionStyle1Dialog.setContentView(permissionStyle1View)
        permissionStyle1Dialog.setCancelable(false)
        val window = permissionStyle1Dialog?.window
        window?.setGravity(Gravity.CENTER)

        val params = window?.attributes
        params?.width = kIntWidth(0.833f)
        permissionStyle1Dialog?.window?.attributes = params
        return permissionStyle1Dialog
    }

    //申请权限弹框样式2-内容可配置
    fun permissionStyle2Dialog(imgUrl: String, defaultResId: Int, title: String, desc: String, btnName: String, process: () -> Unit): AlertDialog? {
        val permissionStyle2Dialog = AlertDialog.Builder(context, R.style.DialogTheme).show()
        val permissionStyle2View = context.UI {
            relativeLayout {
                layoutParams = relativeLP(matchParent, wrapContent)
                var containerLayout = relativeLayout {
                    kRandomId()
                    backgroundResource = R.drawable.shape_bg_white_radius_8
                    var titleLabel = textView(title) {
                        kRandomId()
                        gravity = Gravity.CENTER
                        relateTextSize = CustomTSDimens.SIZE_4F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(10)
                        topMargin = dip(67)
                    }

                    var descLabel = textView(desc) {
                        kRandomId()
                        gravity = Gravity.CENTER
                        relateTextSize = CustomTSDimens.SIZE_2F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(28)
                        topMargin = dip(12)
                        below(titleLabel)
                    }

                    stateButton {
                        kRandomId()
                        text = btnName
                        gravity = Gravity.CENTER
                        setRoundRadius()
                        setNormalBackgroundColor(context.getResColor(R.color.colorGreen))
                        setPressedBackgroundColor(context.getResColor(R.color.colorGreen))
                        setNormalTextColor(Color.WHITE)
                        setPressedTextColor(Color.WHITE)
                        setOnClickListener {
                            permissionStyle2Dialog.dismiss()
                            process.invoke()
                        }
                    }.lparams(matchParent, dip(38)) {
                        horizontalMargin = dip(40)
                        topMargin = dip(18)
                        bottomMargin = dip(20)
                        below(descLabel)
                    }


                }.lparams(matchParent, wrapContent){
                    topMargin = dip(42)
                }

                imageView{
                    Glide.with(this).load(imgUrl).error(defaultResId).into(this)
                }.lparams(dip(109), dip(84)){
                    centerHorizontally()
                }

                imageView{
                    imageResource = R.drawable.icon_close_tanchuang
                    setOnClickListener {
                        permissionStyle2Dialog.dismiss()
                    }
                }.lparams(dip(36), dip(36)){
                    below(containerLayout)
                    centerHorizontally()
                    topMargin = dip(20)
                }
            }
        }.view

        //判断宿主情况
        var isVaild = true
        (context as? Activity)?.also {
            isVaild = !it.isFinishing && (if (Build.VERSION.SDK_INT >= 17) !it.isDestroyed else true)
        }
        if (!isVaild) {
            return null
        }

        permissionStyle2Dialog.setContentView(permissionStyle2View)
        permissionStyle2Dialog.setCancelable(false)
        val window = permissionStyle2Dialog?.window
        window?.setGravity(Gravity.CENTER)

        val params = window?.attributes
        params?.width = kIntWidth(0.833f)
        permissionStyle2Dialog?.window?.attributes = params
        return permissionStyle2Dialog
    }

    //申请悬浮窗权限成功弹框
    fun applyFloatingWindowSuccessDialog(): AlertDialog? {
        val transformation = CornerTransform(context, context.dip(8).toFloat())
        transformation.setExceptCorner(false, false, true, true)

        val applyFloatingWindowSuccessDialog = AlertDialog.Builder(context, R.style.DialogTheme).show()
        val applyFloatingWindowSuccessView = context.UI {
            relativeLayout {
                layoutParams = relativeLP(matchParent, wrapContent)
                var containerLayout = relativeLayout {
                    kRandomId()
                    backgroundResource = R.drawable.shape_bg_white_radius_8

                    var imageView = imageView{
                        kRandomId()
                        Glide.with(this)
                            .load(R.drawable.ic_xuanfuchuang_success)
                            .skipMemoryCache(true)
                            .transform(transformation)
                            .into(this)
                    }.lparams(matchParent, kIntWidth(0.833f)*170/300){}

                    var titleLabel = textView("已升级测速诊断能力") {
                        kRandomId()
                        gravity = Gravity.CENTER
                        relateTextSize = CustomTSDimens.SIZE_4F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(10)
                        topMargin = dip(20)
                        below(imageView)
                    }

                    var descLabel = textView("悬浮窗权限开启成功") {
                        kRandomId()
                        gravity = Gravity.CENTER
                        relateTextSize = CustomTSDimens.SIZE_2F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(35)
                        topMargin = dip(12)
                        bottomMargin = dip(30)
                        below(titleLabel)
                    }

                }.lparams(matchParent, wrapContent){}

                imageView{
                    imageResource = R.drawable.icon_close_tanchuang
                    setOnClickListener {
                        applyFloatingWindowSuccessDialog.dismiss()
                    }
                }.lparams(dip(36), dip(36)){
                    below(containerLayout)
                    centerHorizontally()
                    topMargin = dip(18)
                }
            }
        }.view

        //判断宿主情况
        var isVaild = true
        (context as? Activity)?.also {
            isVaild = !it.isFinishing && (if (Build.VERSION.SDK_INT >= 17) !it.isDestroyed else true)
        }
        if (!isVaild) {
            return null
        }

        applyFloatingWindowSuccessDialog.setContentView(applyFloatingWindowSuccessView)
        applyFloatingWindowSuccessDialog.setCancelable(false)
        val window = applyFloatingWindowSuccessDialog?.window
        window?.setGravity(Gravity.CENTER)

        val params = window?.attributes
        params?.width = kIntWidth(0.833f)
        applyFloatingWindowSuccessDialog?.window?.attributes = params
        return applyFloatingWindowSuccessDialog
    }

    //申请系统权限弹框
    fun applySystemPermissionDialog(descript: String, process: () -> Unit): AlertDialog? {
        val transformation = CornerTransform(context, context.dip(8).toFloat())
        transformation.setExceptCorner(false, false, true, true)

        val applySystemPermissionDialog = AlertDialog.Builder(context, R.style.DialogTheme).show()
        val applySystemPermissionView = context.UI {
            relativeLayout {
                layoutParams = relativeLP(matchParent, wrapContent)
                backgroundResource = R.drawable.shape_bg_white_radius_8
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    imageView{
                        Glide.with(this)
                            .load(R.drawable.ic_apply_system_permission)
                            .skipMemoryCache(true)
                            .transform(transformation)
                            .into(this)
                    }.lparams(matchParent, kIntWidth(0.833f)*170/300){}

                    textView("权限申请") {
                        relateTextSize = CustomTSDimens.SIZE_4F
                        textColor = Color.parseColor("#333333")
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(16)
                        topMargin = dip(16)
                    }

                    textView(descript) {
                        relateTextSize = CustomTSDimens.SIZE_2F
                        textColor = Color.parseColor("#333333")
                    }.lparams(matchParent, wrapContent){
                        horizontalMargin = dip(16)
                        topMargin = dip(8)
                    }

                    stateButton {
                        kRandomId()
                        text = "去授权"
                        gravity = Gravity.CENTER
                        setRadiusByApp()
                        setNormalBackgroundColor(context.getResColor(R.color.colorGreen))
                        setPressedBackgroundColor(context.getResColor(R.color.colorGreen))
                        setNormalTextColor(Color.WHITE)
                        setPressedTextColor(Color.WHITE)
                        setOnClickListener {
                            process.invoke()
                            applySystemPermissionDialog.dismiss()
                        }
                    }.lparams(matchParent, dip(40)) {
                        horizontalMargin = dip(13)
                        verticalMargin = dip(15)
                    }

                }.lparams(matchParent, wrapContent){}

                imageView{
                    imageResource = R.drawable.icon_close_white
                    setOnClickListener {
                        process.invoke()
                        applySystemPermissionDialog.dismiss()
                    }
                }.lparams(dip(16), dip(16)){
                    alignParentRight()
                    topMargin = dip(10)
                    rightMargin = dip(10)
                }

            }
        }.view

        //判断宿主情况
        var isVaild = true
        (context as? Activity)?.also {
            isVaild = !it.isFinishing && (if (Build.VERSION.SDK_INT >= 17) !it.isDestroyed else true)
        }
        if (!isVaild) {
            return null
        }

        applySystemPermissionDialog.setContentView(applySystemPermissionView)
        applySystemPermissionDialog.setCancelable(false)
        val window = applySystemPermissionDialog?.window
        window?.setGravity(Gravity.CENTER)

        val params = window?.attributes
        params?.width = kIntWidth(0.833f)
        applySystemPermissionDialog?.window?.attributes = params
        return applySystemPermissionDialog
    }

}
