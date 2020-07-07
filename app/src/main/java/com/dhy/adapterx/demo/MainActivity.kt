package com.dhy.adapterx.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhy.adapterx.AdapterX
import com.dhy.adapterx.DividerItemDecorationX
import com.dhy.adapterx.IViewHolder
import com.dhy.adapterx.LayoutId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init(recyclerView, Holder::class)
        init(recyclerViewAnno, HolderAnno::class)
    }

    private fun init(rv: RecyclerView, holder: KClass<out IViewHolder<Int>>) {
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecorationX(this))

        val datas = (1..2).toList()
        val adapter: AdapterX<out IViewHolder<Int>, Int> = AdapterX(this, holder, datas)
        rv.adapter = adapter
        adapter.setOnItemClickListener {
            Toast.makeText(this, "position ${it.postion}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *  通过【参数】指定layout，方便查看。【支持】Lib 项目。推荐使用，App和库项目都支持。
     * */
    private class Holder(v: View) : IViewHolder<Int>(v, R.layout.item) {
        override fun update(data: Int, position: Int) {
            tvName.text = "data: $data"
            tvCode.text = "position $position"
        }
    }

    /**
     *  通过【注解】指定layout，方便查看。 【不支持】Lib 项目。
     * */
    @LayoutId(R.layout.item)
    private class HolderAnno(v: View) : IViewHolder<Int>(v) {
        override fun update(data: Int, position: Int) {
            tvName.text = "data: $data"
            tvCode.text = "position $position"
        }
    }
}

