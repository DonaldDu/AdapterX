package com.dhy.adapterx.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.dhy.adapterx.AdapterX
import com.dhy.adapterx.IViewHolder
import com.dhy.adapterx.LayoutId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datas = (1..5).toList().map { it.toString() }
        val adapterX = AdapterX(this, Holder::class, datas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterX
        adapterX.setOnItemClickListener { }
    }

    @LayoutId(R.layout.item)
    private class Holder(v: View) : IViewHolder<String>(v) {
        override fun update(data: String, position: Int) {
            itemView.run {
                tvName.text = data
                tvCode.text = position.toString()
            }
        }
    }
}

