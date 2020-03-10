package com.dhy.adapterx

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View

data class AdapterParams<HOLDER : RecyclerView.ViewHolder>(val holderCreator: ((View) -> HOLDER), @LayoutRes val layoutId: Int)