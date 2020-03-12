package com.dhy.adapterx.demo

import android.view.View
import com.dhy.adapterx.IViewHolder
import kotlinx.android.synthetic.main.item.view.*

class HolderErrorTest(v: View) : IViewHolder<Int>(v, R.layout.item) {
    init {
        itemView.run {
            tvName.text = "test error"
        }
    }

    override fun update(data: Int, position: Int) {
        itemView.run {
            tvName.text = "data: $data"
            tvCode.text = "position $position"
        }
    }
}