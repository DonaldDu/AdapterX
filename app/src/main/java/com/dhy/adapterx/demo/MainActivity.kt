package com.dhy.adapterx.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        val datas = (1..5).toList()
        val adapter = AdapterX(this, Holder::class, datas)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { }
    }

    /**
     *通过注解的方式指定ViewHolder的layout，方便查看。
     * */
    @LayoutId(R.layout.item)
    private class Holder(v: View) : IViewHolder<Int>(v) {
        override fun update(data: Int, position: Int) {
            itemView.run {
                tvName.text = "data: $data"
                tvCode.text = "position $position"
            }
        }
    }
}

