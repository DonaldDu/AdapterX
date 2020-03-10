package com.dhy.adapterx

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class IViewHolder<DATA>(itemView: View, @LayoutRes internal val layoutId: Int = -1) : RecyclerView.ViewHolder(itemView) {
    /**
     * imp IAdapterDatas to use this parameter
     * */
    lateinit var getDatas: () -> MutableList<DATA>
    abstract fun update(data: DATA, position: Int)
}