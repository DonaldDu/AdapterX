package com.dhy.adapterx

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

data class AdapterParams<HOLDER : RecyclerView.ViewHolder>(val holderCreator: ((View) -> HOLDER), @LayoutRes val layoutId: Int)