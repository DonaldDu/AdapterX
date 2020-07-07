package com.dhy.adapterx

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

class PagedAdapterX<DATA : IDiff<DATA>, HOLDER : IViewHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : PagedListAdapter<DATA, HOLDER>(DiffCallback()), IAdapterX<DATA, HOLDER> {
    override var datas: MutableList<DATA> = mutableListOf()
    private val helper = AdapterHelper(this, context, holder, *args)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return helper.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        helper.onBindViewHolder(holder, position)
    }

    override fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?) {
        helper.setOnItemClickListener(onItemClickListener)
    }

    override fun getItemData(position: Int): DATA {
        return getItem(position)!!
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