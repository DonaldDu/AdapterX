package com.dhy.adapterx

import android.content.Context
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

class PagedAdapterX<DATA : IDiff<DATA>, HOLDER : IViewHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : PagedListAdapter<DATA, HOLDER>(DiffCallback()),
    IAdapterX<DATA, HOLDER> by AdapterXHelper(context, holder, null, *args) {
    override var datas: MutableList<DATA>
        get() {
            return currentList?.toMutableList() ?: mutableListOf()
        }
        set(value) {
            throw IllegalStateException("not supported for PagedListAdapter")
        }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        val data = getItem(position)!!
        onBindItemClickListener(holder, data, position)
        holder.update(data, position)
    }
}

interface IDiff<T : IDiff<T>> {
    fun isSame(other: T): Boolean
}

class DiffCallback<T : IDiff<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }
}