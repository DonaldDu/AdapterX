package com.dhy.adapterx

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

open class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : RecyclerView.Adapter<HOLDER>(),
    IAdapterHelper<DATA, HOLDER> by AdapterHelper(context, holder, list, *args) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return onCreateHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        onBindHolder(holder, position)
    }

    override fun getItemData(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

open class DatasAdapterX<HOLDER : IViewHolderDatas<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : RecyclerView.Adapter<HOLDER>(),
    IAdapterHelper<DATA, HOLDER> by DatasAdapterHelper(context, holder, list, *args) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return onCreateHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        onBindHolder(holder, position)
    }

    override fun getItemData(position: Int): DATA {
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}