package com.example.testapp.data.persistent

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {

    /**
     * example
     *  val USERNAME = stringPreferencesKey("username")
     *  val AGE = intPreferencesKey("age")
     */

    val USER_ACCESS_TOKEN = stringPreferencesKey("access_token")
    val USER_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}