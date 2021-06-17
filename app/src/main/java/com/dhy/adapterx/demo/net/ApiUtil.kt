package com.dhy.adapterx.demo.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiUtil {
    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        builder.build()
    }

    private const val BASE_URL = "https://www.wanandroid.com/";

    private fun <T> createService(clazz: Class<T>): T {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(clazz) as T
    }

    val api: Api by lazy {
        createService(Api::class.java)
    }
}