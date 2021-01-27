package com.kagan.chatapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import com.kagan.chatapp.models.UserAuthenticationVM
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.map
import java.util.*

class CurrentUserPreference constructor(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "currentUser"
    )

    companion object {
        val USER_NAME = preferencesKey<String>("USER_NAME")
        val DISPLAY_NAME = preferencesKey<String>("DISPLAY_NAME")
        val ABOUT = preferencesKey<String>("ABOUT")
        val ACCESS_TOKEN = preferencesKey<String>("ACCESS_TOKEN")
        val REFRESH_TOKEN = preferencesKey<String>("REFRESH_TOKEN")
        val ID = preferencesKey<String>("ID")

    }

    val userNameFlow = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val displayNameFlow = dataStore.data.map { preferences ->
        preferences[DISPLAY_NAME]
    }

    val aboutFlow = dataStore.data.map { preferences ->
        preferences[ABOUT]
    }

    val accessTokenFlow = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    val refreshTokenFlow = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    val idFlow = dataStore.data.map { preferences ->
        preferences[ID]
    }

    suspend fun storeUserName(
        username: String
    ) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = username
        }
    }

    suspend fun storeDisplayName(
        displayName: String
    ) {
        dataStore.edit { preferences ->
            preferences[DISPLAY_NAME] = displayName
        }
    }

    suspend fun storeAbout(
        about: String
    ) {
        dataStore.edit { preferences ->
            preferences[ABOUT] = about
        }
    }

    suspend fun storeAccessToken(
        accessToken: String
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun storeRefreshToken(
        refreshToken: String
    ) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun storeId(
        id: String
    ) {
        dataStore.edit { preferences ->
            preferences[ID] = id
        }
    }
}