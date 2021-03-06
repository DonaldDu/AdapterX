package com.dhy.adapterx

import android.content.Context
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

@Deprecated("use PagingDataAdapterX", replaceWith = ReplaceWith("PagingDataAdapterX"))
class PagedAdapterX<DATA : IDiff<DATA>, HOLDER : IViewHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : PagedListAdapter<DATA, HOLDER>(DiffCallback()),
    IAdapterX<HOLDER, DATA> by AdapterXHelper(context, holder, null, *args) {
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

@Deprecated(message = "use IDiff2", replaceWith = ReplaceWith("IDiff2"))
interface IDiff<T : IDiff<T>> {
    @Deprecated("may be error when update item status")
    fun isSame(other: T): Boolean
}

@Deprecated(message = "use DiffCallback2", replaceWith = ReplaceWith("DiffCallback2"))
class DiffCallback<T : IDiff<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }
}