package com.auntec.permissionsguide.ui

import android.view.View
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.DimensAdapter
import com.auntec.permissionsguide.extension.getResColor
import com.auntec.permissionsguide.extension.navigateBar
import com.auntec.permissionsguide.ui.custom.NavigateBar
import org.jetbrains.anko.*

abstract class BaseNavAct(var navTitle: String) : BaseAct() {

    lateinit var navigate: NavigateBar
    private lateinit var statusView: View

    override fun initView() {
//        fullScreenTheme()
//        transparentStatusBar()
        verticalLayout {
            verticalLayout {
                statusView = view { backgroundColorResource = R.color.colorPrimary }.lparams(
                    matchParent,
                    DimensAdapter.staus_Height
                )

                navigate = navigateBar(navTitle) {
                    backgroundColorResource = R.color.colorPrimary
                    setTitleColor(getResColor(R.color.text_color))
                    addLeftDefaultBtn(R.drawable.icon_back) { }.setOnClickListener {
                        clickBack()
                    }
                    initNavigate(this)
                }.lparams(matchParent, DimensAdapter.nav_height)

            }


            initContent().invoke(this)

        }

    }

    abstract fun initContent(): _LinearLayout.() -> Unit

    //初始化导航条
    open fun initNavigate(navView: NavigateBar) {}

    override fun clickBack(): Boolean {
        finish()
        return true
    }
}