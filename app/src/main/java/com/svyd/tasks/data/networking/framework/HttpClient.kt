package com.svyd.tasks.data.networking.framework

import com.svyd.tasks.data.networking.ApiConstants
import com.svyd.tasks.data.networking.header.HeaderInterceptor
import com.svyd.tasks.presentation.TasksApplication

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Singleton class that provides OkHttpClient.
 * Uses [com.svyd.tasks.data.networking.token.TokenManager] in [HeaderInterceptor]
 * to add access token to header.
 */

class HttpClient private constructor() {

    val client: OkHttpClient

    init {
        if (sInstance != null) {
            throw RuntimeException()
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = ApiConstants.LOGGING_LEVEL

        client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(HeaderInterceptor(TasksApplication.instance.tokenManager))
                .connectTimeout(ApiConstants.CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(ApiConstants.READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(ApiConstants.WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .build()
    }

    companion object {

        private var sInstance: HttpClient? = null

        val instance: HttpClient
            get() {
                if (sInstance == null) {
                    synchronized(HttpClient::class.java) {
                        if (sInstance == null) {
                            sInstance = HttpClient()
                        }
                    }
                }
                return sInstance!!
            }
    }

}