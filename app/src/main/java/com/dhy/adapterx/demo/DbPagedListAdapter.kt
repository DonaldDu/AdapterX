package com.dhy.adapterx.demo

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dhy.adapterx.IViewHolder

class DbPagedListAdapter<T : IDiff, VH : IViewHolder<T>> : PagedListAdapter<T, VH>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }
}

interface IDiff {
    fun isSame(other: IDiff): Boolean
}

class DiffCallback<T : IDiff> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isSame(newItem)
    }
}