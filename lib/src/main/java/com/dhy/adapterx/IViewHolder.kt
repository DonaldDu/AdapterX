package com.dhy.adapterx

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class IHolder<DATA>(override val containerView: View, @LayoutRes internal val layoutId: Int) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    init {
        if (itemView is HolderCreatorView) itemView.tag = layoutId
    }
}

abstract class IViewHolder<DATA>(itemView: View, @LayoutRes layoutId: Int = -1) : IHolder<DATA>(itemView, layoutId) {
    abstract fun update(data: DATA, position: Int)
}

abstract class IDatasViewHolder<DATA>(itemView: View, @LayoutRes layoutId: Int = -1) : IHolder<DATA>(itemView, layoutId) {
    abstract fun update(data: DATA, position: Int, datas: List<DATA>)
}