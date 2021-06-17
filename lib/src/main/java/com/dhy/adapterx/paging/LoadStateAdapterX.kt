package com.dhy.adapterx.paging

import android.content.Context
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.dhy.adapterx.AdapterXHelper
import com.dhy.adapterx.IViewHolder
import kotlin.reflect.KClass

class LoadStateAdapterX<HOLDER : IViewHolder<LoadState>>(
    context: Context,
    holder: KClass<HOLDER>,
    vararg args: Any?
) : LoadStateAdapter<HOLDER>() {
    private val helper = AdapterXHelper(context, holder, null, *args)
    override fun onBindViewHolder(holder: HOLDER, loadState: LoadState) {
        holder.update(loadState, -1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): HOLDER {
        return helper.onCreateViewHolder(parent, 0)
    }
}