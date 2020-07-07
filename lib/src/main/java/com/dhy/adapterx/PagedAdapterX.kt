package com.dhy.adapterx

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

class PagedAdapterX<DATA : IDiff, HOLDER : IViewHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : PagedListAdapter<DATA, HOLDER>(DiffCallback()),
    IAdapterHelper<DATA, HOLDER> by AdapterHelper(context, holder, list, *args) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return onCreateHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        onBindHolder(holder, position)
    }

    override fun getItemData(position: Int): DATA {
        return getItem(position)!!
    }
}

interface IDiff {
    fun isSame(other: IDiff): Boolean
}

class DiffCallback<T : IDiff> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }
}