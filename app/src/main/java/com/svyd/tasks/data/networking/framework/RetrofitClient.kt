package com.svyd.tasks.data.networking.framework

import com.svyd.tasks.data.networking.ApiConstants

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

/**
 * A singleton class that provides Retrofit instance.
 */

class RetrofitClient private constructor(client: OkHttpClient, factory: Converter.Factory) {

    val retrofit: Retrofit

    init {
        if (sInstance != null) {
            throw RuntimeException()
        }

        retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.API_ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(factory)
                .client(client)
                .build()
    }

    companion object {
        private var sInstance: RetrofitClient? = null

        fun getInstance(client: OkHttpClient, factory: Converter.Factory): RetrofitClient {
            if (sInstance == null) {
                synchronized(RetrofitClient::class.java) {
                    if (sInstance == null) {
                        sInstance = RetrofitClient(client, factory)
                    }
                }
            }
            return sInstance!!
        }
    }
}
