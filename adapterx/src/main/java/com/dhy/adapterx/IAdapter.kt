package com.dhy.adapterx


import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class IAdapter<HOLDER : RecyclerView.ViewHolder, DATA>(
    context: Context,
    list: List<DATA>?,
    @param:LayoutRes val layoutId: Int,
    private val holderCreator: ((View) -> HOLDER)
) : RecyclerView.Adapter<HOLDER>() {

    var datas: MutableList<DATA> = list as? MutableList ?: (list?.toMutableList() ?: mutableListOf())
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        val itemView = inflater.inflate(layoutId, parent, false)
        return holderCreator(itemView)
    }

    @CallSuper
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        if (onItemClickListener != null) {
            holder.itemView.setTagX(position)
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    fun getItemData(position: Int): DATA {
        return datas[position]
    }

    private val onClickListener = View.OnClickListener {
        val position: Int = it.getTagX()
        onItemClickListener?.invoke(ClickedItem(it, position))
    }

    private var onItemClickListener: ((ClickedItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ClickedItem) -> Unit) {
        onItemClickListener = listener
    }

    fun getItem(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

data class ClickedItem(val v: View, val postion: Int)

inline fun <reified T> View.getTagX(): T {
    val datas = findRecyclerViewHolderTag()!!
    return datas[T::class.java.name] as T
}

inline fun <reified T> View.getTagXOrNull(): T? {
    val datas = findRecyclerViewHolderTag(false)
    return datas?.get(T::class.java.name) as T?
}

fun View.setTagX(value: Any) {
    val datas = findRecyclerViewHolderTag()!!
    datas[value.javaClass.name] = value
}

/**
 *@param sure of RecyclerViewHolder
 * */
fun View.findRecyclerViewHolderTag(sure: Boolean = true): MutableMap<String, Any>? {
    val item = findViewByParent { it is RecyclerView }
    return if (item == null) {
        if (sure) throw IllegalStateException("tagx only used for RecyclerViewHolder")
        else null
    } else {
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

fun View.findViewByParent(test: (parent: ViewGroup) -> Boolean): View? {
    val p = parent
    return if (p is ViewGroup) {
        if (test(p)) this
        else p.findViewByParent(test)
    } else null
}