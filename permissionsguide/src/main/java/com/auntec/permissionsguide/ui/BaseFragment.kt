package com.auntec.permissionsguide.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jetbrains.anko._FrameLayout
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import java.lang.ref.WeakReference

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return UI {
            frameLayout {
                lparams(width = matchParent, height = matchParent)
                initContent().invoke(this)
                initData()
            }

        }.view
    }

    open fun initData() {

    }

    abstract fun initContent(): _FrameLayout.() -> Unit

    var weakSelf = WeakReference<BaseFragment>(this)

    open fun willShow() {}
    open fun onShow(showCode: Int = 0) {}
    open fun onHide() {}
    open fun onKeyBackPressed(): Boolean {
        return false
    }

    open fun clickTab() {}
}