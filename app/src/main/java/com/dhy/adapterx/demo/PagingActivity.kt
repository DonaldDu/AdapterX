package com.dhy.adapterx.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhy.adapterx.IViewHolder
import com.dhy.adapterx.PagedAdapterX
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.android.synthetic.main.item.*


class PagingActivity : AppCompatActivity() {
    private lateinit var adapter: PagedAdapterX<Page, Holder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PagedAdapterX(this, Holder::class)
        recyclerView.adapter = adapter
        initPaging()
    }

    private fun initPaging() {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPrefetchDistance(5)
            .build()
        val liveData: LiveData<PagedList<Page>> = LivePagedListBuilder(DbDataSourceFactory(), config).build()
        liveData.observe(this, Observer<PagedList<Page>> { datas ->
            adapter.submitList(datas)
        })
    }

    private class Holder(v: View) : IViewHolder<Page>(v, R.layout.item) {
        override fun update(data: Page, position: Int) {
            tvName.text = "id: ${data.id}"
            tvCode.text = "position $position"
        }
    }
}