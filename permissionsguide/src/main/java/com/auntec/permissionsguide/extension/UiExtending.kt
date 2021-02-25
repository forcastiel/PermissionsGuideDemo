package com.auntec.permissionsguide.extension

import android.content.Context
import android.view.View
import android.view.ViewManager
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import com.auntec.permissionsguide.ui.custom.NavigateBar
import com.auntec.permissionsguide.ui.custom.StateButton
import org.jetbrains.anko.custom.ankoView


var nonTheme = 0
inline fun ViewManager.navigateBar() = navigateBar {}
inline fun ViewManager.navigateBar(init: NavigateBar.() -> Unit) = ankoView({ NavigateBar(it) }, nonTheme , init)

fun ViewManager.navigateBar(title:String,init: (NavigateBar.() -> Unit)? = null ): NavigateBar {
    return ankoView({ NavigateBar(it) }, nonTheme){
        setTitle(title)
        //setNavBgColor(CommonUiSetup.pramaryColor)
        init?.invoke(this)
    }
}

inline fun ViewManager.stateButton() = stateButton {}

inline fun ViewManager.stateButton(init: StateButton.() -> Unit) =
    ankoView({ StateButton(it) }, 0, init)

open class _CardView(ctx: Context) : CardView(ctx) {
    fun <T : View> T.lparams(
        c: android.content.Context?,
        attrs: android.util.AttributeSet?,
        init: FrameLayout.LayoutParams.() -> Unit = {}
    ): T {
        val layoutParams = FrameLayout.LayoutParams(c!!, attrs!!)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    fun <T : View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        init: FrameLayout.LayoutParams.() -> Unit = {}
    ): T {
        val layoutParams = FrameLayout.LayoutParams(width, height)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }
}

inline fun ViewManager.cardView() = cardView {}
inline fun ViewManager.cardView(init: _CardView.() -> Unit) = ankoView({ _CardView(it) }, 0, init)