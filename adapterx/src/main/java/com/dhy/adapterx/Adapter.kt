package com.dhy.adapterx

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

open class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : IAdapter<HOLDER, DATA>(context, list, getLayoutId(holder), getHolderCreator(holder, *args)) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(getItem(position), position)
    }
}

open class AdapterWithDatas<HOLDER : IViewHolderWithDatas<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : IAdapter<HOLDER, DATA>(context, list, getLayoutId(holder), getHolderCreator(holder, *args)) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(getItem(position), position, datas)
    }
}

abstract class IViewHolder<DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun update(data: DATA, position: Int)

    fun getAdapter(): AdapterX<*, DATA> {
        val rv = itemView.parent as RecyclerView
        @Suppress("UNCHECKED_CAST")
        return rv.adapter as AdapterX<*, DATA>
    }
}

abstract class IViewHolderWithDatas<DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun update(data: DATA, position: Int, datas: List<DATA>)
}

@LayoutRes
fun <HOLDER : RecyclerView.ViewHolder> getLayoutId(holder: KClass<HOLDER>): Int {
    return holder.java.getAnnotation(LayoutId::class.java)!!.value
}

fun <HOLDER : RecyclerView.ViewHolder> getHolderCreator(holder: KClass<HOLDER>, vararg args: Any?): ((View) -> HOLDER) {
    val constructor = holder.primaryConstructor!!
    if (!constructor.isAccessible) constructor.isAccessible = true
    if (args.isEmpty()) return { constructor.call(it) }
    else {
        val params = args.toMutableList().apply { add(0, null) }.toTypedArray()
        return {
            params[0] = it
            constructor.call(*params)
        }
    }
}
