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
) : IAdapter<HOLDER, DATA>(context, list, getLayoutId(context, holder), getHolderCreator(holder, *args)) {
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
) : IAdapter<HOLDER, DATA>(context, list, getLayoutId(context, holder), getHolderCreator(holder, *args)) {
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
fun <HOLDER : RecyclerView.ViewHolder> getLayoutId(context: Context?, holder: KClass<HOLDER>): Int {
    val cls = holder.java
    return if (cls.isAnnotationPresent(LayoutId::class.java)) {
        cls.getAnnotation(LayoutId::class.java)!!.value
    } else {
        val name = cls.getAnnotation(LayoutName::class.java)!!.value
        context!!.resources.getIdentifier(name, "layout", context.packageName)
    }
}

@Suppress("UNCHECKED_CAST")
fun <HOLDER : RecyclerView.ViewHolder> getHolderCreator(holder: KClass<HOLDER>, vararg args: Any?): ((View) -> HOLDER) {
    val constructor = holder.java.declaredConstructors.first()
    if (!constructor.isAccessible) constructor.isAccessible = true
    if (args.isEmpty()) return { constructor.newInstance(it) as HOLDER }
    else {
        val params = args.toMutableList().apply { add(0, null) }.toTypedArray()
        return {
            params[0] = it
            constructor.newInstance(*params) as HOLDER
        }
    }
}
