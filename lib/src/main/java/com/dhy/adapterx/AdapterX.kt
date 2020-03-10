package com.dhy.adapterx

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.reflect.KClass

open class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : IAdapter<HOLDER, DATA>(context, list, getAdapterParams(context, holder, *args)) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(getItem(position), position)
    }
}