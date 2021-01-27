package com.kagan.chatapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.map

class TokenPreference(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "token")

    companion object {
        val ACCESS_TOKEN = preferencesKey<String>("access_token")
        val REFRESH_TOKEN = preferencesKey<String>("refresh_token")
    }

    val accessTokenFlow = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }
    val refreshTokenFlow = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    suspend fun storeAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun storeRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    suspend fun removeAccessToken() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = ""
        }
    }
}