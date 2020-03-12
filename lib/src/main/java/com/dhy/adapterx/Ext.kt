package com.dhy.adapterx

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import java.lang.Exception
import kotlin.reflect.KClass

val RecyclerView.ViewHolder.context: Context get() = itemView.context

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

fun <HOLDER : RecyclerView.ViewHolder> getAdapterParams(context: Context?, holder: KClass<HOLDER>, vararg args: Any?): AdapterParams<HOLDER> {
    val creator = getHolderCreator(holder, *args)
    var layoutId = -1
    if (context != null) {
        val v = HolderCreatorView(context)
        layoutId = try {
            val h = creator(v) as IViewHolder<*>
            h.layoutId
        } catch (e: Exception) {
            v.tag as Int
        }
    }
    if (layoutId == -1) {
        val cls = holder.java
        @Suppress("DEPRECATION")
        layoutId = when {
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
    return AdapterParams(creator, layoutId)
}