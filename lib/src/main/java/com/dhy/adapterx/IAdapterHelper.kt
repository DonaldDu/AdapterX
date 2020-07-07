package com.dhy.adapterx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

interface IAdapterHelper<DATA, HOLDER : IHolder<DATA>> {
    var datas: MutableList<DATA>
    fun onCreateHolder(parent: ViewGroup, viewType: Int): HOLDER
    fun onBindHolder(holder: HOLDER, position: Int)

    fun getItemData(position: Int): DATA

    open fun getClickedItem(v: View): ClickedItem<DATA> {
        val position: Int = v.getTagX()
        return ClickedItem(v, position, getItemData(position))
    }

    fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?)
}

open class BaseAdapterHelper<DATA, HOLDER : IHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : IAdapterHelper<DATA, HOLDER> {
    private val params = getAdapterParams(context, holder, *args)

    @LayoutRes
    private val layoutId = params.layoutId
    private val holderCreator = params.holderCreator
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override var datas: MutableList<DATA> = list as? MutableList ?: (list?.toMutableList() ?: mutableListOf())
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): HOLDER {
        val itemView = inflater.inflate(layoutId, parent, false)
        return holderCreator(itemView)
    }

    override fun onBindHolder(holder: HOLDER, position: Int) {
        if (itemClickListener != null) {
            holder.itemView.setTagX(position)
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    override fun getItemData(position: Int): DATA {
        return datas[position]
    }

    private var itemClickListener: ((ClickedItem<DATA>) -> Unit)? = null
    override fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?) {
        this.itemClickListener = onItemClickListener
    }

    private val onClickListener = View.OnClickListener {
        if (itemClickListener != null) itemClickListener!!.invoke(getClickedItem(it))
    }
}

open class AdapterHelper<DATA, HOLDER : IViewHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : BaseAdapterHelper<DATA, HOLDER>(context, holder, list, *args) {
    override fun onBindHolder(holder: HOLDER, position: Int) {
        super.onBindHolder(holder, position)
        holder.update(getItemData(position), position)
    }
}

open class DatasAdapterHelper<DATA, HOLDER : IViewHolderDatas<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : BaseAdapterHelper<DATA, HOLDER>(context, holder, list, *args) {
    override fun onBindHolder(holder: HOLDER, position: Int) {
        super.onBindHolder(holder, position)
        holder.update(getItemData(position), position, datas)
    }
}

data class ClickedItem<DATA>(val v: View, val postion: Int, val data: DATA)
