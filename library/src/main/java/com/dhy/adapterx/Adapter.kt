package com.dhy.adapterx

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class IViewHolder<DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun update(data: DATA, position: Int)
}

open class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(context: Context, holder: KClass<HOLDER>, list: List<DATA>? = null, vararg args: Any?) : IAdapter<HOLDER, DATA>(context, list, getLayoutId(holder), getHolderCreator(holder, *args)) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(getItem(position), position)
    }
}

@LayoutRes
private fun <HOLDER : RecyclerView.ViewHolder> getLayoutId(holder: KClass<HOLDER>): Int {
    return holder.java.getAnnotation(LayoutId::class.java)!!.value
}

private fun <HOLDER : RecyclerView.ViewHolder> getHolderCreator(holder: KClass<HOLDER>, vararg args: Any?): ((View) -> HOLDER) {
    if (args.isEmpty()) {
        return {
            holder.primaryConstructor!!.call(it)
        }
    } else {
        val params = args.toMutableList().apply { add(0, null) }.toTypedArray()
        val constructor = holder.primaryConstructor!!
        return {
            params[0] = it
            constructor.call(*params)
        }
    }
}
