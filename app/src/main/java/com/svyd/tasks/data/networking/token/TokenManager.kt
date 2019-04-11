package com.svyd.tasks.data.networking.token

interface TokenManager {
    fun provideToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}
