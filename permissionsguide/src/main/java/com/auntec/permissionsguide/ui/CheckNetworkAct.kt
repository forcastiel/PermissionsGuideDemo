package com.auntec.permissionsguide.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.auntec.permissionsguide.PermissionGuideAbility
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.bo.ApplyStep
import com.auntec.permissionsguide.bo.PermissionBo
import com.auntec.permissionsguide.bo.key
import com.auntec.permissionsguide.extension.*
import com.auntec.permissionsguide.ui.adaptation.pixelPadding
import com.auntec.permissionsguide.ui.custom.ZsxTip
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.jetbrains.anko.*

class CheckNetworkAct: BaseAct(), PermissionGuideAbility {

    var permissionsGranted: ArrayList<Pair<PermissionBo, Boolean>> = arrayListOf()
    var permissionsUnGranted: ArrayList<Pair<PermissionBo, Boolean>> = arrayListOf()

    private lateinit var containerLayout: ScrollView
    private lateinit var countLabel: TextView

    override fun initView() {
        relativeLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)
            backgroundColorResource = R.color.colorBg
            var headLayout = verticalLayout {
                kRandomId()
                backgroundResource = R.drawable.shape_bg_check_network
                view {}.lparams(matchParent, DimensAdapter.staus_Height)

                linearLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    relativeLayout {
                        imageView {
                            imageResource = R.drawable.icon_back
                        }.lparams(dip(16), dip(16)){
                            centerInParent()
                        }
                        setOnClickListener { backClick() }
                    }.lparams(dip(24), dip(24)){
                        leftMargin = dip(16)
                    }

                    textView("网络检测宝典") {
                        relateTextSize = CustomTSDimens.SIZE_4F
                        textColorResource = R.color.text_color
                    }.lparams(){
                        leftMargin = dip(16)
                    }
                }.lparams(matchParent, DimensAdapter.nav_height)

                relativeLayout {
                    verticalLayout {
                        linearLayout {
                            kRandomId()
                            gravity = Gravity.CENTER_VERTICAL
                            textView("网络测速诊断能力受限") {
                                relateTextSize = CustomTSDimens.SIZE_3F
                                textColorResource = R.color.text_color
                            }.lparams(){}
                            countLabel = textView("") {
                                pixelPadding(dip(5), dip(1), dip(5), dip(1))
                                backgroundResource = R.drawable.shape_bg_white_trans_radius_30
                                relateTextSize = CustomTSDimens.SIZE_L2F
                                textColorResource = R.color.text_color
                            }.lparams(){
                                leftMargin = dip(6)
                            }
                        }.lparams(){
                            bottomMargin = dip(6)
                        }
                        textView("开启网络检测宝典，提升测速诊断能力") {
                            relateTextSize = CustomTSDimens.SIZE_L1F
                            textColorResource = R.color.text_color
                        }.lparams(){}
                    }.lparams(){
                        leftMargin = dip(13)
                        centerVertically()
                    }

                    imageView {
                        imageResource = R.drawable.ic_baodian
                    }.lparams(dip(94), dip(53)){
                        alignParentRight()
                        centerVertically()
                        rightMargin = dip(28)
                    }
                }.lparams(matchParent, dip(60)){
                    topMargin = dip(5)
                    bottomMargin = dip(67)
                }
            }.lparams(matchParent, wrapContent){}

            scrollView {
                containerLayout = this
            }.lparams(matchParent, wrapContent){
                below(headLayout)
                topMargin = -dip(42)
                horizontalMargin = dip(13)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        uiThread(500) {
            refresh()
        }
    }

    fun refresh(){
        permissionsGranted.clear()
        permissionsUnGranted.clear()
        arrayListOf(PermissionBo.FLOATING_WINDOW, PermissionBo.BACKGROUND_POPUP, PermissionBo.AUTO_RUN, PermissionBo.LOCK_SCREEN, PermissionBo.WRITE_EXTERNAL_STORAGE, PermissionBo.READ_PHONE_STATE).forEach { bo ->
            when(bo){
                PermissionBo.FLOATING_WINDOW -> if(permissionGuideDao.isFloatingWindowOpen(this)) permissionsGranted.add(bo to true) else permissionsUnGranted.add(bo to false)
                PermissionBo.BACKGROUND_POPUP -> if(permissionGuideDao.isBackgroundPopupOpen(this)) permissionsGranted.add(bo to true) else permissionsUnGranted.add(bo to false)
                PermissionBo.AUTO_RUN -> if(permissionGuideDao.isAutoRunOpen(this)) permissionsGranted.add(bo to true) else permissionsUnGranted.add(bo to false)
                PermissionBo.LOCK_SCREEN -> if(permissionGuideDao.isLockScreenOpen(this)) permissionsGranted.add(bo to true) else permissionsUnGranted.add(bo to false)
                PermissionBo.WRITE_EXTERNAL_STORAGE -> if(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) permissionsGranted.add(bo to true) else permissionsUnGranted.add(bo to false)
                PermissionBo.READ_PHONE_STATE -> if(checkPermission(Manifest.permission.READ_PHONE_STATE)) permissionsGranted.add(bo to true) else permissionsUnGranted.add(bo to false)
            }
        }
        buildMainPart()
    }

    fun buildMainPart(){
        containerLayout.removeAllViews()
        containerLayout.addView(UI {
            verticalLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
                if(permissionsUnGranted.isNotEmpty()){
                    verticalLayout {
                        backgroundResource = R.drawable.shape_bg_white_radius_6
                        textView("必备权限") {
                            relateTextSize = CustomTSDimens.SIZE_0F
                            textColorResource = R.color.colorTxtBlack
                        }.lparams(){
                            topMargin = dip(20)
                            leftMargin = dip(16)
                        }
                        textView("影响网络诊断测速能力，务必开启") {
                            relateTextSize = CustomTSDimens.SIZE_L1F
                            textColorResource = R.color.colorTxtLightGray
                        }.lparams(){
                            topMargin = dip(6)
                            leftMargin = dip(16)
                        }
                        permissionsUnGranted.forEach { buildItem(it).invoke(this) }
                        view {}.lparams(matchParent, dip(20)){}
                    }.lparams(matchParent, wrapContent){
                        bottomMargin = dip(12)
                    }
                }
                if(permissionsGranted.isNotEmpty()){
                    verticalLayout {
                        backgroundResource = R.drawable.shape_bg_white_radius_6
                        textView("已获取权限") {
                            relateTextSize = CustomTSDimens.SIZE_0F
                            textColorResource = R.color.colorTxtBlack
                        }.lparams(){
                            topMargin = dip(20)
                            leftMargin = dip(16)
                        }
                        permissionsGranted.forEach { buildItem(it).invoke(this) }
                        view {}.lparams(matchParent, dip(20)){}
                    }.lparams(matchParent, wrapContent){
                        bottomMargin = dip(12)
                    }
                }
            }
        }.view)
        countLabel.text = "${permissionsGranted.size}/${permissionsGranted.size + permissionsUnGranted.size}"
    }

    fun buildItem(pair: Pair<PermissionBo, Boolean>): _LinearLayout.() -> Unit{
        return {
            relativeLayout {
                var iconView = imageView {
                    kRandomId()
                    imageResource = pair.first.resId
                }.lparams(dip(45), dip(45)){
                    centerVertically()
                    leftMargin = dip(16)
                    rightMargin = dip(12)
                }
                verticalLayout {
                    textView(pair.first.title) {
                        relateTextSize = CustomTSDimens.SIZE_0F
                        textColorResource = R.color.colorTxtBlack
                    }.lparams(){}
                    textView(pair.first.descript) {
                        relateTextSize = CustomTSDimens.SIZE_L1F
                        textColorResource = R.color.colorTxtLightGray
                    }.lparams(){
                        topMargin = dip(6)
                    }
                }.lparams(){
                    rightOf(iconView)
                    centerVertically()
                }
                stateButton {
                    kRandomId()
                    text = if(pair.second) "已开启" else "开启"
                    gravity = Gravity.CENTER
                    isEnabled = !pair.second
                    setRoundRadius()
                    setNormalBackgroundColor(getResColor(R.color.colorBtnEnabled))
                    setUnableBackgroundColor(getResColor(R.color.colorBtnUnEnabled))

                    setNormalTextColor(Color.WHITE)
                    setUnableTextColor(getResColor(R.color.colorTxtUnEnabled))

                    setOnClickListener {
                        applyPermission(pair.first)
                    }
                }.lparams(dip(64), dip(28)) {
                    alignParentRight()
                    centerVertically()
                    rightMargin = dip(20)
                }
            }.lparams(matchParent, dip(71)){}
        }
    }

    private fun applyPermission(bo: PermissionBo){
        when(bo){
            PermissionBo.FLOATING_WINDOW -> {
                permissionGuideDao.applyFloatingWindow(this)
            }
            PermissionBo.BACKGROUND_POPUP -> {
                permissionGuideDao.applyBackgroundPopup(this)
            }
            PermissionBo.AUTO_RUN -> {
                permissionGuideDao.applyAutoRun(this, ApplyStep.FIRST_APPLY.value){
                    refresh()
                }
            }
            PermissionBo.LOCK_SCREEN -> {
                permissionGuideDao.applyLockScreen(this)
            }
            PermissionBo.WRITE_EXTERNAL_STORAGE -> {
                AndPermission.with(this)
                    .runtime()
                    .permission(arrayOf(Permission.WRITE_EXTERNAL_STORAGE))
                    .onGranted {}
                    .onDenied {}
                    .start()
            }
            PermissionBo.READ_PHONE_STATE -> {
                AndPermission.with(this)
                    .runtime()
                    .permission(arrayOf(Permission.READ_PHONE_STATE))
                    .onGranted {}
                    .onDenied {}
                    .start()
            }
        }
    }

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

    var hasShowTip = false  //退出页面时，如果还有未授权的权限，提示弹框是否已显示过

    private fun backClick(){
        if(permissionsUnGranted.isNotEmpty() && !hasShowTip){
            hasShowTip = true

            var permissionBo = permissionGuideDao.getPermissionToShow(this)
            if(permissionBo != PermissionBo.NULL){
                ZsxTip(this).permissionStyle2Dialog("", R.drawable.ic_tishi_fail, "还有重要权限未开启", "请授权开启更多测速诊断重要权限", "立即开启"){
//                    permissionGuideDao.showApplyPermissionTip(this, permissionBo)

                    var sharePreferences = getSharedPreferences("SpeedTest", Context.MODE_PRIVATE)
                    //保存当天权限申请次数
                    sharePreferences.edit().putInt(permissionBo.key(), sharePreferences.getInt(permissionBo.key(), 0) + 1).commit()

                    when(permissionBo){
                        PermissionBo.FLOATING_WINDOW -> permissionGuideDao.applyFloatingWindow(this)
                        PermissionBo.BACKGROUND_POPUP -> permissionGuideDao.applyBackgroundPopup(this)
                        PermissionBo.AUTO_RUN -> permissionGuideDao.applyAutoRun(this, ApplyStep.FIRST_APPLY.value)
                        PermissionBo.LOCK_SCREEN -> permissionGuideDao.applyLockScreen(this)
                    }
                }
            }else{
                finish()
            }
        }else{
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backClick()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

}