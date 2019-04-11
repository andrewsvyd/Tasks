package com.svyd.tasks.data.networking.token

import android.content.SharedPreferences

class PreferenceTokenManager(private val preferences: SharedPreferences) : TokenManager {

    override fun provideToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    override fun saveToken(token: String) {
        preferences
                .edit()
                .putString(KEY_TOKEN, TOKEN_PREFIX + token)
                .apply()
    }

    override fun clearToken() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val TOKEN_PREFIX = "Bearer "
    }
}
