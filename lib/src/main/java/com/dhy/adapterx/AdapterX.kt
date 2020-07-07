package com.dhy.adapterx

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

class AdapterX<HOLDER : IViewHolder<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : RecyclerView.Adapter<HOLDER>(), IAdapterX<DATA, HOLDER> {
    override var datas: MutableList<DATA> = list as? MutableList ?: (list?.toMutableList() ?: mutableListOf())
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
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}

class DatasAdapterX<HOLDER : IViewHolderDatas<DATA>, DATA>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : RecyclerView.Adapter<HOLDER>(), IAdapterX<DATA, HOLDER> {
    override var datas: MutableList<DATA> = list as? MutableList ?: (list?.toMutableList() ?: mutableListOf())
    private val helper = DatasAdapterHelper(this, context, holder, *args)
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
        return datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}