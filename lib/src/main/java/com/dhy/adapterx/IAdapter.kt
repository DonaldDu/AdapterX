package com.dhy.adapterx


import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class IAdapter<HOLDER : RecyclerView.ViewHolder, DATA>(
    context: Context, list: List<DATA>?, params: AdapterParams<HOLDER>
) : RecyclerView.Adapter<HOLDER>() {
    @LayoutRes
    val layoutId = params.layoutId
    private val holderCreator = params.holderCreator
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var datas: MutableList<DATA> = list as? MutableList ?: (list?.toMutableList() ?: mutableListOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        val itemView = inflater.inflate(layoutId, parent, false)
        val holder = holderCreator(itemView)
        if (holder is IAdapterDatas<*>) loadDatas2Holder(holder)
        return holder
    }

    private fun loadDatas2Holder(holder: HOLDER) {
        @Suppress("UNCHECKED_CAST")
        val h = holder as IViewHolder<DATA>
        h.getDatas = { datas }
    }

    @CallSuper
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        if (onItemClickListener != null) {
            holder.itemView.setTagX(position)
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    open fun getClickedItem(v: View): ClickedItem<DATA> {
        val position: Int = v.getTagX()
        return ClickedItem(v, position, getItem(position))
    }

    private val onClickListener = View.OnClickListener {
        if (onItemClickListener != null) onItemClickListener!!.invoke(getClickedItem(it))
    }

    private var onItemClickListener: ((ClickedItem<DATA>) -> Unit)? = null
    fun setOnItemClickListener(listener: (ClickedItem<DATA>) -> Unit) {
        onItemClickListener = listener
    }

    open fun getItem(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

data class ClickedItem<DATA>(val v: View, val postion: Int, val data: DATA)

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