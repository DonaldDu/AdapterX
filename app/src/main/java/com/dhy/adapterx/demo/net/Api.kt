package com.dhy.adapterx.demo.net

import androidx.annotation.IntRange
import com.dhy.adapterx.demo.data.DemoReqData
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    /**
     * 获取数据
     */
    @GET("wenda/list/{pageId}/json")
    suspend fun getData(@Path("pageId") @IntRange(from = 0) pageId: Int): DemoReqData
}