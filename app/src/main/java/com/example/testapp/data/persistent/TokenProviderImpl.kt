package com.example.testapp.data.persistent

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenProviderImpl(private val context: Context) : TokenProvider {

    override suspend fun getAccessToken(): String? {
        return context.dataStore.data
            .map { it[PreferenceKeys.USER_ACCESS_TOKEN] }
            .firstOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data
            .map { it[PreferenceKeys.USER_REFRESH_TOKEN] }
            .firstOrNull()
    }

    override suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_ACCESS_TOKEN] = token
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_REFRESH_TOKEN] = token
        }
    }

    override suspend fun clearTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(PreferenceKeys.USER_ACCESS_TOKEN)
            prefs.remove(PreferenceKeys.USER_REFRESH_TOKEN)
        }
    }
}
