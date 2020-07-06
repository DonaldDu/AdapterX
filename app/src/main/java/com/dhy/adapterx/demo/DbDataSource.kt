package com.dhy.adapterx.demo

import android.arch.paging.PositionalDataSource

class DbDataSource : PositionalDataSource<Page>() {
    private val db = DbUtil()
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Page>) {
        val datas = db.getImagePages(0, params.requestedLoadSize)
        callback.onResult(datas, 0, datas.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Page>) {
        val datas = db.getImagePages(params.startPosition, params.loadSize)
        callback.onResult(datas)
    }
}

class DbUtil {
    fun getImagePages(offset: Int, limit: Int): List<Page> {
        return (offset until offset.plus(limit)).map { Page(it) }
    }
}