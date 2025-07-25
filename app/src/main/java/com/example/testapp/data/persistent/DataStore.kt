package com.example.testapp.data.persistent

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// This creates a singleton DataStore<Preferences>
val Context.dataStore by preferencesDataStore(name = "app_prefs")