package com.svyd.tasks.data.networking.framework


import com.google.gson.GsonBuilder

import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper class that is capable of creating [retrofit2.Retrofit] services
 */

class ServiceProvider {

    /**
     * Create new Retrofit service for given interface.
     * @param clazz interface of Retrofit service
     * @return new instance of service
     */

    fun <T> provideService(clazz: Class<T>): T {
        val builder = GsonBuilder()
        return RetrofitClient.getInstance(
                HttpClient.instance.client,
                GsonConverterFactory.create(builder.create()))
                .retrofit
                .create(clazz)
    }
}
