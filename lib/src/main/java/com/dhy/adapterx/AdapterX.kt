package com.dhy.adapterx

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

open class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : RecyclerView.Adapter<HOLDER>(),
    IAdapterX<DATA, HOLDER> by AdapterXHelper(context, holder, list, *args) {

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        val data = getItem(position)
        onBindItemClickListener(holder, data, position)
        holder.update(data, position)
    }

    open fun getItem(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

open class DatasAdapterX<HOLDER : IDatasViewHolder<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : RecyclerView.Adapter<HOLDER>(),
    IAdapterX<DATA, HOLDER> by AdapterXHelper(context, holder, list, *args) {

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        val data = getItem(position)
        onBindItemClickListener(holder, data, position)
        holder.update(data, position, datas)
    }

    open fun getItem(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}