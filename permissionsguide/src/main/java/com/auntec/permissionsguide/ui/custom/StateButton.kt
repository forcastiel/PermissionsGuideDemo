package com.auntec.permissionsguide.ui.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatButton
import com.auntec.permissionsguide.R
import com.auntec.permissionsguide.extension.kHeight
import com.auntec.permissionsguide.extension.kIntHeight
import org.jetbrains.anko.verticalPadding

/**
 * @author deadline
 * @time 2016-11-07
 */

class StateButton : AppCompatButton {

    enum class PaddingMode(val vPadding: Float) {
        NORMAL(0.021f), SMALL(0.01f)
    }

    //text color
    private var mNormalTextColor = 0
    private var mPressedTextColor = 0
    private var mUnableTextColor = 0
    internal var mTextColorStateList: ColorStateList? = null

    //animation duration
    private var mDuration = 0

    private var mRound: Boolean = false

    //stroke
    private var mStrokeDashWidth = 0f
    private var mStrokeDashGap = 0f
    private var mNormalStrokeWidth = 0
    private var mPressedStrokeWidth = 0
    private var mUnableStrokeWidth = 0
    private var mNormalStrokeColor = 0
    private var mPressedStrokeColor = 0
    private var mUnableStrokeColor = 0

    //background color
    private var mNormalBackgroundColor = 0
    private var mPressedBackgroundColor = 0
    private var mUnableBackgroundColor = 0

    private var mNormalBackground: GradientDrawable? = null
    private var mPressedBackground: GradientDrawable? = null
    private var mUnableBackground: GradientDrawable? = null

    private var states: Array<IntArray?> = arrayOf()

    internal var mStateBackground: StateListDrawable? = null

    constructor(context: Context, normalColor: Int, preColor: Int, disableColor: Int) : super(
        context,
        null,
        0
    ) {
        setup(null)
        this.mNormalBackgroundColor = normalColor
        this.mPressedBackgroundColor = preColor
        this.mUnableBackgroundColor = disableColor
        mNormalBackground!!.setColor(mNormalBackgroundColor)
        mPressedBackground!!.setColor(mPressedBackgroundColor)
        mUnableBackground!!.setColor(mUnableBackgroundColor)
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.buttonStyle
    ) : super(context, attrs, R.style.BlankButton) {
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {

        states = arrayOfNulls(4)

        val drawable = background
        if (drawable != null && drawable is StateListDrawable) {
            mStateBackground = drawable
        } else {
            mStateBackground = StateListDrawable()
        }

        mNormalBackground = GradientDrawable()
        mPressedBackground = GradientDrawable()
        mUnableBackground = GradientDrawable()

        //pressed, focused, normal, unable
        states[0] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
        states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states[3] = intArrayOf(-android.R.attr.state_enabled)
        states[2] = intArrayOf(android.R.attr.state_enabled)

        val a = context.obtainStyledAttributes(attrs, R.styleable.StateButton)

        //get original text color as default
        //set text color
        mTextColorStateList = textColors
        val mDefaultNormalTextColor =
            mTextColorStateList!!.getColorForState(states[2], currentTextColor)
        val mDefaultPressedTextColor =
            mTextColorStateList!!.getColorForState(states[0], currentTextColor)
        val mDefaultUnableTextColor =
            mTextColorStateList!!.getColorForState(states[3], currentTextColor)
        mNormalTextColor =
            a.getColor(R.styleable.StateButton_normalTextColor, mDefaultNormalTextColor)
        mPressedTextColor =
            a.getColor(R.styleable.StateButton_pressedTextColor, mDefaultPressedTextColor)
        mUnableTextColor =
            a.getColor(R.styleable.StateButton_unableTextColor, mDefaultUnableTextColor)
        setTextColor()

        //set animation duration
        mDuration = a.getInteger(R.styleable.StateButton_animationDuration, mDuration)
        mStateBackground!!.setEnterFadeDuration(mDuration)
        mStateBackground!!.setExitFadeDuration(mDuration)

        //set background color
        mNormalBackgroundColor = a.getColor(R.styleable.StateButton_normalBackgroundColor, 0)
        mPressedBackgroundColor = a.getColor(R.styleable.StateButton_pressedBackgroundColor, 0)
        mUnableBackgroundColor = a.getColor(R.styleable.StateButton_unableBackgroundColor, 0)
        mNormalBackground!!.setColor(mNormalBackgroundColor)
        mPressedBackground!!.setColor(mPressedBackgroundColor)
        mUnableBackground!!.setColor(mUnableBackgroundColor)

        //set radius
        val mRadius = a.getDimensionPixelSize(R.styleable.StateButton_state_radius, 0).toFloat()
        mRound = a.getBoolean(R.styleable.StateButton_round, false)
        mNormalBackground!!.cornerRadius = mRadius
        mPressedBackground!!.cornerRadius = mRadius
        mUnableBackground!!.cornerRadius = mRadius
        //set stroke
        mStrokeDashWidth =
            a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0).toFloat()
        mStrokeDashGap =
            a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0).toFloat()
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_normalStrokeWidth, 0)
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_pressedStrokeWidth, 0)
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_unableStrokeWidth, 0)
        mNormalStrokeColor = a.getColor(R.styleable.StateButton_normalStrokeColor, 0)
        mPressedStrokeColor = a.getColor(R.styleable.StateButton_pressedStrokeColor, 0)
        mUnableStrokeColor = a.getColor(R.styleable.StateButton_unableStrokeColor, 0)
        setStroke()

        //set background
        mStateBackground?.addState(states[0], mPressedBackground)
        mStateBackground?.addState(states[1], mPressedBackground)
        mStateBackground?.addState(states[3], mUnableBackground)
        mStateBackground?.addState(states[2], mNormalBackground)
        setBackgroundDrawable(mStateBackground)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setRound(mRound)
    }

    /****************** stroke color  */

    fun setNormalStrokeColor(@ColorInt normalStrokeColor: Int) {
        this.mNormalStrokeColor = normalStrokeColor
        setStroke(mNormalBackground!!, mNormalStrokeColor, mNormalStrokeWidth)
    }

    fun setPressedStrokeColor(@ColorInt pressedStrokeColor: Int) {
        this.mPressedStrokeColor = pressedStrokeColor
        setStroke(mPressedBackground!!, mPressedStrokeColor, mPressedStrokeWidth)
    }

    fun setUnableStrokeColor(@ColorInt unableStrokeColor: Int) {
        this.mUnableStrokeColor = unableStrokeColor
        setStroke(mUnableBackground!!, mUnableStrokeColor, mUnableStrokeWidth)
    }

    fun setStateStrokeColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int) {
        mNormalStrokeColor = normal
        mPressedStrokeColor = pressed
        mUnableStrokeColor = unable
        setStroke()
    }

    /****************** stroke width  */

    fun setNormalStrokeWidth(normalStrokeWidth: Int) {
        this.mNormalStrokeWidth = normalStrokeWidth
        setStroke(mNormalBackground!!, mNormalStrokeColor, mNormalStrokeWidth)
    }

    fun setPressedStrokeWidth(pressedStrokeWidth: Int) {
        this.mPressedStrokeWidth = pressedStrokeWidth
        setStroke(mPressedBackground!!, mPressedStrokeColor, mPressedStrokeWidth)
    }

    fun setUnableStrokeWidth(unableStrokeWidth: Int) {
        this.mUnableStrokeWidth = unableStrokeWidth
        setStroke(mUnableBackground!!, mUnableStrokeColor, mUnableStrokeWidth)
    }

    fun setStateStrokeWidth(normal: Int, pressed: Int, unable: Int) {
        mNormalStrokeWidth = normal
        mPressedStrokeWidth = pressed
        mUnableStrokeWidth = unable
        setStroke()
    }

    fun setStrokeDash(strokeDashWidth: Float, strokeDashGap: Float) {
        this.mStrokeDashWidth = strokeDashWidth
        this.mStrokeDashGap = strokeDashWidth
        setStroke()
    }

    private fun setStroke() {
        setStroke(mNormalBackground!!, mNormalStrokeColor, mNormalStrokeWidth)
        setStroke(mPressedBackground!!, mPressedStrokeColor, mPressedStrokeWidth)
        setStroke(mUnableBackground!!, mUnableStrokeColor, mUnableStrokeWidth)
    }

    private fun setStroke(mBackground: GradientDrawable, mStrokeColor: Int, mStrokeWidth: Int) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap)
    }

    /********************   radius   */

    fun setRadius(@FloatRange(from = 0.0) radius: Float) {
        mNormalBackground!!.cornerRadius = radius
        mPressedBackground!!.cornerRadius = radius
        mUnableBackground!!.cornerRadius = radius
    }

    fun setRadius(
        @FloatRange(from = 0.0) leftTop: Float,
        @FloatRange(from = 0.0) rightTop: Float,
        @FloatRange(from = 0.0) rightBottom: Float,
        @FloatRange(from = 0.0) leftBottom: Float
    ) {
        val radii = floatArrayOf(
            leftTop,
            leftTop,
            rightTop,
            rightTop,
            rightBottom,
            rightBottom,
            leftBottom,
            leftBottom
        )
        mNormalBackground!!.cornerRadii = radii
        mPressedBackground!!.cornerRadii = radii
        mUnableBackground!!.cornerRadii = radii
    }

    fun setRound(round: Boolean) {
        this.mRound = round
        val height = measuredHeight
        if (mRound) {
            setRadius(height / 2f)
        }
    }

    fun setRadius(radii: FloatArray) {
        mNormalBackground!!.cornerRadii = radii
        mPressedBackground!!.cornerRadii = radii
        mUnableBackground!!.cornerRadii = radii
    }

    /********************  background color   */

    fun setStateBackgroundColor(
        @ColorInt normal: Int,
        @ColorInt pressed: Int,
        @ColorInt unable: Int
    ) {
        mNormalBackgroundColor = normal
        mPressedBackgroundColor = pressed
        mUnableBackgroundColor = unable
        mNormalBackground!!.setColor(mNormalBackgroundColor)
        mPressedBackground!!.setColor(mPressedBackgroundColor)
        mUnableBackground!!.setColor(mUnableBackgroundColor)
    }

    fun setNormalBackgroundColor(@ColorInt normalBackgroundColor: Int) {
        this.mNormalBackgroundColor = normalBackgroundColor
        mNormalBackground!!.setColor(mNormalBackgroundColor)
    }

    fun setNormalBackgroundColors(@ColorInt normalBackgroundColors: IntArray) {
        mNormalBackground!!.colors = normalBackgroundColors
        mNormalBackground!!.orientation = GradientDrawable.Orientation.LEFT_RIGHT
    }

    fun setNormalBackgroundColorsTop2Bottom(@ColorInt normalBackgroundColors: IntArray) {
        val normalBackground = IntArray(11)
        normalBackground[0] = 1
        mNormalBackground!!.colors = normalBackgroundColors
        mNormalBackground!!.orientation = GradientDrawable.Orientation.TOP_BOTTOM
    }

    fun setPressedBackgroundColors(@ColorInt normalBackgroundColors: IntArray) {
        mPressedBackground!!.colors = normalBackgroundColors
        mPressedBackground!!.orientation = GradientDrawable.Orientation.LEFT_RIGHT
    }

    fun setPressedBackgroundColorsToptoBottom(@ColorInt normalBackgroundColors: IntArray) {
        mPressedBackground!!.colors = normalBackgroundColors
        mPressedBackground!!.orientation = GradientDrawable.Orientation.TOP_BOTTOM
    }

    fun setUnableBackgroundColors(@ColorInt normalBackgroundColors: IntArray) {
        mUnableBackground!!.colors = normalBackgroundColors
        mUnableBackground!!.orientation = GradientDrawable.Orientation.LEFT_RIGHT
    }

    fun setPressedBackgroundColor(@ColorInt pressedBackgroundColor: Int) {
        this.mPressedBackgroundColor = pressedBackgroundColor
        mPressedBackground!!.setColor(mPressedBackgroundColor)
    }

    fun setUnableBackgroundColor(@ColorInt unableBackgroundColor: Int) {
        this.mUnableBackgroundColor = unableBackgroundColor
        mUnableBackground!!.setColor(mUnableBackgroundColor)
    }

    /*******************alpha animation duration */
    fun setAnimationDuration(@IntRange(from = 0) duration: Int) {
        this.mDuration = duration
        mStateBackground?.setEnterFadeDuration(mDuration)
    }

    /***************  text color    */

    private fun setTextColor() {
        val colors =
            intArrayOf(mPressedTextColor, mPressedTextColor, mNormalTextColor, mUnableTextColor)
        mTextColorStateList = ColorStateList(states, colors)
        setTextColor(mTextColorStateList)
    }

    fun setStateTextColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int) {
        this.mNormalTextColor = normal
        this.mPressedTextColor = pressed
        this.mUnableTextColor = unable
        setTextColor()
    }

    fun setNormalTextColor(@ColorInt normalTextColor: Int) {
        this.mNormalTextColor = normalTextColor
        setTextColor()

    }

    fun setPressedTextColor(@ColorInt pressedTextColor: Int) {
        this.mPressedTextColor = pressedTextColor
        setTextColor()
    }

    fun setUnableTextColor(@ColorInt unableTextColor: Int) {
        this.mUnableTextColor = unableTextColor
        setTextColor()
    }

    fun setRadiusByApp() {
        this.setRadius(kHeight(0.01f))
    }

    fun setRoundRadius() {
        this.setRadius(kHeight(0.03f))
    }

    fun setPaddingByMode(paddingMode: PaddingMode = PaddingMode.NORMAL) {
        this.verticalPadding = kIntHeight(paddingMode.vPadding)
    }
}
