package com.svyd.tasks.data.networking.header

import com.svyd.tasks.data.networking.token.TokenManager

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor which adds access token to header.
 */

class HeaderInterceptor(private val provider: TokenManager) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = provider.provideToken()?.let { token -> chain.request()
                .newBuilder()
                .addHeader(AUTHORIZATION_TOKEN, token)
                .build() } ?: chain.request()
                .newBuilder()
                .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_TOKEN = "Authorization"
    }
}