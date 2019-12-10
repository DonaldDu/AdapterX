package com.dhy.adapterx.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.dhy.adapterx.AdapterX
import com.dhy.adapterx.DividerItemDecorationX
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
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecorationX(this))
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            Toast.makeText(this, "position ${it.postion}", Toast.LENGTH_SHORT).show()
            val loader = classLoader
            println(loader.parent)
        }
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

