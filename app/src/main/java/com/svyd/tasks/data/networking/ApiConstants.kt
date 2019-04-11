package com.svyd.tasks.data.networking

import okhttp3.logging.HttpLoggingInterceptor

class ApiConstants private constructor() {

    init {
        //in case of reflection
        throw RuntimeException()
    }

    companion object {

        private val PROTOCOL = "http://"
        private val LIVE_INSTANCE = "testapi.doitserver.in.ua/api/"

        val API_ENDPOINT = PROTOCOL + LIVE_INSTANCE

        val LOGGING_LEVEL: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

        val CONNECTION_TIME_OUT = 30
        val READ_TIME_OUT = 30
        val WRITE_TIME_OUT = 30
    }
}
