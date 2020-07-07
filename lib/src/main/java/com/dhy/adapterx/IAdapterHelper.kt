package com.dhy.adapterx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

interface IAdapterX<DATA, HOLDER : IHolder<DATA>> {
    var datas: MutableList<DATA>
    fun getItemData(position: Int): DATA
    fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?)
}

interface IAdapterHelper<DATA, HOLDER : IHolder<DATA>> {
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER
    fun onBindViewHolder(holder: HOLDER, position: Int)
    fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?)
}

open class BaseAdapterHelper<DATA, HOLDER : IHolder<DATA>>(
    val adapter: IAdapterX<DATA, HOLDER>,
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : IAdapterHelper<DATA, HOLDER> {
    private val params = getAdapterParams(context, holder, *args)

    @LayoutRes
    private val layoutId = params.layoutId
    private val holderCreator = params.holderCreator
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        val itemView = inflater.inflate(layoutId, parent, false)
        return holderCreator(itemView)
    }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        if (itemClickListener != null) {
            holder.itemView.setTagX(position)
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    open fun getClickedItem(v: View): ClickedItem<DATA> {
        val position: Int = v.getTagX()
        return ClickedItem(v, position, adapter.getItemData(position))
    }

    private var itemClickListener: ((ClickedItem<DATA>) -> Unit)? = null
    override fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?) {
        this.itemClickListener = onItemClickListener
    }

    private val onClickListener = View.OnClickListener {
        if (itemClickListener != null) itemClickListener!!.invoke(getClickedItem(it))
    }
}

class AdapterHelper<DATA, HOLDER : IViewHolder<DATA>>(
    adapter: IAdapterX<DATA, HOLDER>,
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : BaseAdapterHelper<DATA, HOLDER>(adapter, context, holder, *args) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(adapter.getItemData(position), position)
    }
}

class DatasAdapterHelper<DATA, HOLDER : IViewHolderDatas<DATA>>(
    adapter: IAdapterX<DATA, HOLDER>,
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : BaseAdapterHelper<DATA, HOLDER>(adapter, context, holder, *args) {
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.update(adapter.getItemData(position), position, adapter.datas)
    }
}

data class ClickedItem<DATA>(val v: View, val postion: Int, val data: DATA)
