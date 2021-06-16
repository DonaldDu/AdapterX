package com.dhy.adapterx.demo

import androidx.paging.PagingSource

class Paging3Source : PagingSource<Int, String>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val page = params.key ?: 0
            val data = (page * params.loadSize until (page + 1) * params.loadSize).map { it.toString() }
            LoadResult.Page(data = data, prevKey = null, nextKey = page + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}