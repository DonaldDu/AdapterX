package com.dhy.adapterx

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
            else -> throw IllegalArgumentException("getLayoutId failed for holder: ${cls.name}")
        }
    }
    return AdapterParams(creator, layoutId)
}

inline fun <reified T> View.getTagX(): T {
    val datas = findRecyclerViewHolderTag()!!
    return datas[T::class.java.name] as T
}

inline fun <reified T> View.getTagxOrNull(): T? {
    val datas = findRecyclerViewHolderTag()
    return datas?.get(T::class.java.name) as T?
}

fun View.setTagX(value: Any) {
    val datas = findRecyclerViewHolderTag()
    if (datas != null) datas[value.javaClass.name] = value
}

fun View.findRecyclerViewHolderTag(): MutableMap<String, Any>? {
    val item = findRvItemView()
    return if (item == null) null
    else {
        val stored = item.getTag(R.id.RECYCLER_VIEW_VIEW_HOLDER_TAGX)
        if (stored == null) {
            val d = mutableMapOf<String, Any>()
            item.setTag(R.id.RECYCLER_VIEW_VIEW_HOLDER_TAGX, d)
            d
        } else {
            @Suppress("UNCHECKED_CAST")
            stored as MutableMap<String, Any>
        }
    }
}

private fun View.findRvItemView(): View? {
    return if (layoutParams is RecyclerView.LayoutParams) this
    else {
        val p = parent as? View
        p?.findRvItemView()
    }
}