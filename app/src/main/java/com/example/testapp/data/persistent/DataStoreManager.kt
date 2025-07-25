package com.example.testapp.data.persistent

import android.content.Context
import androidx.datastore.preferences.core.edit


class DataStoreManager(private val context: Context) {

    /**
     * example:
     *     // Save string
     *     suspend fun saveUsername(username: String) {
     *         context.dataStore.edit { prefs ->
     *             prefs[PreferenceKeys.USERNAME] = username
     *         }
     *     }
     *
     *     // Read string
     *     val usernameFlow: Flow<String?> = context.dataStore.data
     *         .map { prefs -> prefs[PreferenceKeys.USERNAME] }
     */


    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_ACCESS_TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_REFRESH_TOKEN] = token
        }
    }
}