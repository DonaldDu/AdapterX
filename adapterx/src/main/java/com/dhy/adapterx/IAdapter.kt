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
            holder.itemView.itemPosition = position
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    fun getItemData(position: Int): DATA {
        return datas[position]
    }

    private val onClickListener = View.OnClickListener {
        val position = it.itemPosition
        onItemClickListener?.invoke(ClickedItem(it, position))
    }

    private var onItemClickListener: ((ClickedItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ClickedItem) -> Unit) {
        onItemClickListener = { listener(it) }
    }

    fun getItem(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

data class ClickedItem(val v: View, val postion: Int)

var View.itemPosition: Int
    get() = getTag(R.id.holder_item_position) as Int
    set(value) = setTag(R.id.holder_item_position, value)

