package com.dhy.adapterx.paging

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dhy.adapterx.AdapterXHelper
import com.dhy.adapterx.ClickedItem
import com.dhy.adapterx.IViewHolder
import kotlin.reflect.KClass

class PagingDataAdapterX<HOLDER : IViewHolder<DATA>, DATA : IDiff2<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : PagingDataAdapter<DATA, HOLDER>(DiffCallback2()) {
    private val helper = AdapterXHelper(context, holder, null, *args)
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        val data = getItem(position)!!
        helper.onBindItemClickListener(holder, data, position)
        holder.update(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return helper.onCreateViewHolder(parent, viewType)
    }

    fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?) {
        helper.setOnItemClickListener(onItemClickListener)
    }
}

interface IDiff2<T : IDiff2<T>> {
    fun areItemsTheSame(other: T): Boolean
    fun areContentsTheSame(other: T): Boolean
}

class DiffCallback2<T : IDiff2<T>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}