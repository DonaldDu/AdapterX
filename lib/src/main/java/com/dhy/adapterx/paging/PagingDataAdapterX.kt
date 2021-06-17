package com.dhy.adapterx.paging

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.dhy.adapterx.AdapterXHelper
import com.dhy.adapterx.DiffCallback
import com.dhy.adapterx.IDiff
import com.dhy.adapterx.IViewHolder
import kotlin.reflect.KClass

class PagingDataAdapterX<HOLDER : IViewHolder<DATA>, DATA : IDiff<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : PagingDataAdapter<DATA, HOLDER>(DiffCallback()) {
    private val helper = AdapterXHelper(context, holder, null, *args)
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        val data = getItem(position)!!
        helper.onBindItemClickListener(holder, data, position)
        holder.update(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return helper.onCreateViewHolder(parent, viewType)
    }
}