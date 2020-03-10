package com.dhy.adapterx

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.util.Log
import android.view.View
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * RecyclerView的ItemDecoration的默认实现
 * 1. 默认使用系统的分割线
 * 2. 支持设置分隔线尺寸。
 * 3. 支持设置是否显示底部的分隔线(showLastDivider default is false)
 * 4. Divider.intrinsicHeight.atLeast(1px)
 */
class DividerItemDecorationX(context: Context, verticalList: Boolean = true, dividerDrawable: Drawable? = null) : ItemDecoration() {
    private lateinit var mDivider: Drawable
    private var mWidth = 0
    private var mHeight = 0
    private var mOrientation = 0

    init {
        if (dividerDrawable != null) mDivider = dividerDrawable
        else {
            val themeDivider = getThemeDivider(context)
            if (themeDivider != null) mDivider = themeDivider
            else {
                Log.w("DividerItem", "android:listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute or call setDrawable()")
            }
        }
        setOrientation(verticalList)
    }

    fun setDrawable(dividerDrawable: Drawable): DividerItemDecorationX {
        mDivider = dividerDrawable
        return this
    }

    private fun getThemeDivider(context: Context): Drawable? {
        val a = context.obtainStyledAttributes(ATTRS)
        val divider = a.getDrawable(0)
        a.recycle()
        return divider
    }

    private fun setOrientation(verticalList: Boolean) {
        mOrientation = if (verticalList) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL
    }

    /**
     * 新增：支持手动为无高宽的drawable制定宽度
     */
    fun setWidth(width: Int) {
        mWidth = width
    }

    /**
     * 新增：支持手动为无高宽的drawable制定高度
     */
    fun setHeight(height: Int) {
        mHeight = height
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private var deltaCount = 1
    var showLastDivider: Boolean
        get() {
            return deltaCount == 0
        }
        set(show) {
            deltaCount = if (show) 0 else 1
        }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount - deltaCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin + child.translationY.roundToInt()
            val bottom = top + dividerHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount - deltaCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin + child.translationX.roundToInt()
            val right = left + dividerWidth
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        if (pos < parent.adapter!!.itemCount - deltaCount) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, dividerHeight)
            } else {
                outRect.set(0, 0, dividerWidth, 0)
            }
        } else {
            outRect.set(0, 0, 0, -1)
        }
    }

    private val dividerWidth: Int
        get() = if (mWidth > 0) mWidth else mDivider.intrinsicWidth.atLeast(1)

    private val dividerHeight: Int
        get() = if (mHeight > 0) mHeight else mDivider.intrinsicHeight.atLeast(1)

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        private const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }

    private fun Int.atLeast(v: Int): Int {
        return max(this, v)
    }
}