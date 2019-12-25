package com.dhy.adapterx

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.reflect.KClass

open class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(
    context: Context, holder: KClass<HOLDER>, list: List<DATA>? = null, vararg args: Any?
) : IAdapter<HOLDER, DATA>(context, list, getLayoutId(context, holder, *args), getHolderCreator(holder, *args)) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(getItem(position), position)
    }
}

abstract class IViewHolder<DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun update(data: DATA, position: Int)
}

abstract class IViewHolder2<DATA>(@LayoutRes internal val layoutId: Int, itemView: View) : IViewHolder<DATA>(itemView)

@Suppress("DEPRECATION")
@LayoutRes
fun <HOLDER : RecyclerView.ViewHolder> getLayoutId(context: Context?, holder: KClass<HOLDER>, vararg args: Any?): Int {
    val cls = holder.java
    return when {
        IViewHolder2::class.java.isAssignableFrom(cls) -> {
            val creator = getHolderCreator(holder, *args)
            val h = creator(View(context!!)) as IViewHolder2<*>
            return h.layoutId
        }
        cls.isAnnotationPresent(LayoutId::class.java) -> {
            cls.getAnnotation(LayoutId::class.java)!!.value
        }
        cls.isAnnotationPresent(LayoutName::class.java) -> {
            val name = cls.getAnnotation(LayoutName::class.java)!!.value
            context!!.resources.getIdentifier(name, "layout", context.packageName)
        }
        else -> throw IllegalArgumentException("getLayoutId failed for holder: ${cls.name}")
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
