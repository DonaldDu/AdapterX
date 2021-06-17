package com.dhy.adapterx.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import com.dhy.adapterx.IViewHolder
import com.dhy.adapterx.demo.net.ApiUtil
import com.dhy.adapterx.paging.LoadStateAdapterX
import com.dhy.adapterx.paging.PagingDataAdapterX
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.android.synthetic.main.item_loadstate.*
import kotlinx.android.synthetic.main.paging_item.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.dhy.adapterx.demo.data.DemoReqData.DatasBean as Page


private const val TAG = "PagingActivity"

class PagingActivity : AppCompatActivity() {
    private lateinit var adapter: PagingDataAdapterX<Holder, Page>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        val loadAdapter = LoadStateAdapterX(this, LoadStateViewHolder::class, View.OnClickListener {
            adapter.retry()
        })
        adapter = PagingDataAdapterX(this, Holder::class)
        recyclerView.adapter = adapter.withLoadStateFooter(loadAdapter)
        initPaging()
    }

    private fun initPaging() {
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    Log.d(TAG, "LoadStateListener is NotLoading")
                }
                is LoadState.Loading -> {
                    Log.d(TAG, "LoadStateListener is Loading")
                }
            }
            val e = it.error?.error
            if (e != null) {
                Log.d(TAG, "LoadStateListener :" + e.message)
                e.printStackTrace()
            }
        }

        lifecycleScope.launch {
            getDataPager().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun getDataPager() = Pager(
        initialKey = 0,
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { NetDataSource() }
    ).flow

    private class Holder(v: View) : IViewHolder<Page>(v, R.layout.paging_item) {
        override fun update(data: Page, position: Int) {
            tvAuthor.text = data.author
            tvTitle.text = data.title
        }
    }

    private class LoadStateViewHolder(v: View, retry: View.OnClickListener) : IViewHolder<LoadState>(v, R.layout.item_loadstate) {
        init {
            btn_retry.setOnClickListener(retry)
        }

        override fun update(data: LoadState, position: Int) {
            when (data) {
                is LoadState.Error -> {
                    ll_loading.visibility = View.GONE
                    btn_retry.visibility = View.VISIBLE
                    Log.d(TAG, "LoadStateViewHolder: show error")
                }
                is LoadState.Loading -> {
                    ll_loading.visibility = View.VISIBLE
                    btn_retry.visibility = View.GONE
                }
                else -> {
                    Log.d(TAG, "LoadStateViewHolder: 其他的错误")
                }
            }
        }
    }
}

class NetDataSource : PagingSource<Int, Page>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Page> {
        return try {
            //页码未定义置为1
            val currentPage = params.key ?: 1
            //仓库层请求数据
            Log.d(TAG, "请求第${currentPage}页")
            val demoReqData = ApiUtil.api.getData(currentPage)

            val totalPage = demoReqData.data?.pageCount ?: 0
            val nextPage = if (currentPage < totalPage) {
                currentPage + 1  //当前页码 小于 总页码 页面加1
            } else {
                null //没有更多数据
            }

            LoadResult.Page(
                data = demoReqData.data.datas,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

val CombinedLoadStates.error: LoadState.Error?
    get() {
        var e = refresh
        if (e is LoadState.Error) return e
        e = append
        return if (e is LoadState.Error) e else null
    }