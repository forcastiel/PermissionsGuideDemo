package com.auntec.permissionsguide.ui

import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.CustomTSDimens
import com.auntec.permissionsguide.extension.DimensAdapter
import com.auntec.permissionsguide.extension.kIntWidth
import com.auntec.permissionsguide.extension.kRandomId
import org.jetbrains.anko.*

abstract class BaseTabAct : BaseAct() {

    lateinit var mainLayout: RelativeLayout
    private lateinit var contentLayout: RelativeLayout
    var contentFragments: List<FragmentRelation> = arrayListOf()
    private var tabRelations: List<TabRelation> = arrayListOf<TabRelation>()
    var currFragment: BaseFragment? = null
    private var currTabRelation: TabRelation? = null
    private var cacheFragment: BaseFragment? = null


    override fun initView() {
//        fullScreenTheme()
//        transparentStatusBar()
        contentFragments = initFragments()

        mainLayout = relativeLayout {
            kRandomId()
            backgroundColorResource = R.color.colorPrimary
            tabRelations = initTabs()

            val tabLayout = linearLayout {
                kRandomId()
                tabRelations.forEach { it.layout().invoke(this@linearLayout) }
            }.lparams(matchParent, wrapContent) { alignParentBottom() }


            contentLayout =
                relativeLayout {
                    kRandomId()

                }.lparams(matchParent, matchParent) {
                    above(tabLayout)
                }


            fragmentOpt { opt ->
                contentFragments.forEach {
                    opt.add(
                        contentLayout.id,
                        it.baseFragment!!
                    )
                    opt.setMaxLifecycle(it.baseFragment!!, Lifecycle.State.CREATED)
                }
            }
        }

        showDefaultFragment(contentFragments)
    }

    abstract fun initFragments(): List<FragmentRelation>

    abstract fun initTabs(): List<TabRelation>

    abstract fun onTabChanged(position: Int, isDefaultOperation: Boolean)

    private fun showDefaultFragment(contentFragments: List<FragmentRelation>) {
        currTabRelation = tabRelations[0]
        tabRelations.forEach { it.select(0) }

        fragmentOpt { trans ->
            contentFragments.forEachIndexed { index, fragment ->
                if (index != 0) {
                    trans.hide(fragment.baseFragment!!)
                }
            }
            setCurrFragment(trans, contentFragments[0].baseFragment!!)
            cacheFragment = contentFragments[0].baseFragment!!
            onTabChanged(0, true)
        }
    }

    //点击底部tablayout切换
    fun switchFragment(layoutLevel: Int) {
        val newTabRelation = tabRelations.get(layoutLevel)
        if (currTabRelation == newTabRelation) {
            return
        }

        fragmentOpt { trans ->
            if (currFragment == null) {
                return@fragmentOpt
            }

            var newFragment: BaseFragment? = null
            contentFragments.forEach {
                if (it.tabIndex == layoutLevel) {
                    if (it.multiTab) {
                        newFragment = cacheFragment
                        return@forEach
                    } else {
                        newFragment = it.baseFragment
                    }
                }
            }

            if (newFragment == null)
                return@fragmentOpt

            currTabRelation = newTabRelation
            tabRelations.forEach { it.select(newTabRelation.layoutLevel) }

            val tempFragment = currFragment!!
            trans.setMaxLifecycle(tempFragment, Lifecycle.State.STARTED)
            trans.hide(tempFragment)

            setCurrFragment(trans, newFragment!!)
            onTabChanged(newTabRelation.layoutLevel, false)
        }
    }

    fun updateFragment(
        newFragment: BaseFragment,
        destroyFragment: Boolean = false,
        needAdd: Boolean = false
    ) {
        fragmentOpt { trans ->
            if (currFragment == null) {
                return@fragmentOpt
            }

            if (destroyFragment) {
                trans.remove(currFragment!!)
                currFragment == null
            } else {
                trans.setMaxLifecycle(currFragment!!, Lifecycle.State.STARTED)
                trans.hide(currFragment!!)
            }

            if (needAdd) {
                trans.add(contentLayout.id, newFragment)
            }

            cacheFragment = newFragment
            this.currFragment?.onHide()

            this.currFragment = newFragment
            trans.setMaxLifecycle(newFragment, Lifecycle.State.RESUMED)
            trans.show(newFragment)
            newFragment.willShow()
            newFragment.onShow()
        }
    }

    private fun setCurrFragment(
        trans: FragmentTransaction,
        showFragment: BaseFragment
    ) {

        if (currFragment == showFragment) {
            return
        }
        this.currFragment?.onHide()
        this.currFragment = showFragment
        trans.setMaxLifecycle(showFragment, Lifecycle.State.RESUMED)
        trans.show(showFragment)
        showFragment.willShow()
        showFragment.onShow()
    }

    /**
     * fragment的操作
     */
    fun fragmentOpt(run: (FragmentTransaction) -> Unit) {
        val trans = supportFragmentManager.beginTransaction()
        run(trans)
        trans.commit()
    }

    class TabRelation {
        private lateinit var imgView: ImageView
        private lateinit var titleView: TextView

        var layoutLevel: Int = 0
        var title: String = ""

        @DrawableRes
        var preIcon: Int

        @DrawableRes
        var norIcon: Int
        lateinit var clickProcess: (TabRelation) -> Unit

        constructor(
            layoutLevel: Int,
            title: String,
            preIcon: Int,
            norIcon: Int,
            clickProcess: (TabRelation) -> Unit
        ) {
            this.layoutLevel = layoutLevel
            this.title = title
            this.preIcon = preIcon
            this.norIcon = norIcon
            this.clickProcess = clickProcess
        }


        fun layout(): _LinearLayout.() -> Unit {
            return {
                verticalLayout {
                    verticalPadding = dip(5)
                    backgroundColorResource = R.color.colorPrimaryDark
                    isClickable = true
                    setOnClickListener {
                        clickProcess(this@TabRelation)
                    }
                    gravity = Gravity.CENTER_HORIZONTAL
                    relativeLayout {
                        imgView = imageView(norIcon) {
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                        }.lparams(kIntWidth(0.1f), kIntWidth(0.08f)) {
                            centerInParent()
                        }

                    }.lparams(kIntWidth(0.1f), kIntWidth(0.08f))

                    titleView = textView(title) {
                        textColor = Color.parseColor("#a2a2a2")
                        textSize = DimensAdapter.textSpSize(CustomTSDimens.SIZE_L1F)
                    }.lparams(wrapContent, wrapContent)
                }.lparams(matchParent, matchParent, 1f)
            }
        }

        fun select(selectLevel: Int) {
            if (selectLevel == layoutLevel) {
                imgView.imageResource = preIcon
                titleView.textColor = Color.parseColor("#ccffffff")
            } else {
                imgView.imageResource = norIcon
                titleView.textColor = Color.parseColor("#a2a2a2")
            }
        }
    }

    class FragmentRelation {

        var baseFragment: BaseFragment? = null
        var tabIndex: Int? = null
        var multiTab: Boolean = false

        constructor(baseFragment: BaseFragment?, tabIndex: Int?) {
            this.baseFragment = baseFragment
            this.tabIndex = tabIndex
        }

        constructor(baseFragment: BaseFragment?, tabIndex: Int?, multiTab: Boolean) {
            this.baseFragment = baseFragment
            this.tabIndex = tabIndex
            this.multiTab = multiTab
        }


    }
}