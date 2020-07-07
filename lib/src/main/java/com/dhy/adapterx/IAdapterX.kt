package com.dhy.adapterx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

interface IAdapterX<DATA, HOLDER : IHolder<DATA>> {
    var datas: MutableList<DATA>
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER
    fun onBindItemClickListener(holder: HOLDER, data: DATA, position: Int)
    fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?)
}

open class AdapterXHelper<DATA, HOLDER : IHolder<DATA>>(
    context: Context,
    holder: KClass<HOLDER>,
    list: List<DATA>? = null,
    vararg args: Any?
) : IAdapterX<DATA, HOLDER> {
    private val params = getAdapterParams(context, holder, *args)
    override var datas: MutableList<DATA> = list as? MutableList ?: (list?.toMutableList() ?: mutableListOf())

    @LayoutRes
    private val layoutId = params.layoutId
    private val holderCreator = params.holderCreator
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        val itemView = inflater.inflate(layoutId, parent, false)
        return holderCreator(itemView)
    }

    override fun onBindItemClickListener(holder: HOLDER, data: DATA, position: Int) {
        if (itemClickListener != null) {
            holder.itemView.setTagX(position)
            holder.itemView.setTag(R.id.RECYCLER_VIEW_HOLDER_DATA, data)
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    private var itemClickListener: ((ClickedItem<DATA>) -> Unit)? = null
    override fun setOnItemClickListener(onItemClickListener: ((ClickedItem<DATA>) -> Unit)?) {
        this.itemClickListener = onItemClickListener
    }

    private fun getClickedItem(v: View): ClickedItem<DATA> {
        @Suppress("UNCHECKED_CAST")
        val data = v.getTag(R.id.RECYCLER_VIEW_HOLDER_DATA) as DATA
        val position: Int = v.getTagX()
        return ClickedItem(v, position, data)
    }

    private val onClickListener = View.OnClickListener {
        itemClickListener?.invoke(getClickedItem(it))
    }
}

data class ClickedItem<DATA>(val v: View, val postion: Int, val data: DATA)
