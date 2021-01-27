package com.kagan.chatapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kagan.chatapp.datastore.CurrentUserPreference
import kotlinx.coroutines.launch

class CurrentUserViewModel
@ViewModelInject
constructor(
    private val userPreference: CurrentUserPreference
) : ViewModel() {

    private val username = userPreference.userNameFlow.asLiveData()
    private val displayName = userPreference.displayNameFlow.asLiveData()
    private val about = userPreference.aboutFlow.asLiveData()
    private val accessToken = userPreference.accessTokenFlow.asLiveData()
    private val refreshToken = userPreference.refreshTokenFlow.asLiveData()
    private val id = userPreference.idFlow.asLiveData()

    fun storeUser(
        username: String,
        displayName: String,
        about: String,
        accessToken: String,
        refreshToken: String,
        id: String
    ) = viewModelScope.launch {
        storeUserName(username)
        storeDisplayName(displayName)
        storeAbout(about)
        storeAccessToken(accessToken)
        storeRefreshToken(refreshToken)
        storeId(id)
    }

    private fun storeUserName(username: String) = viewModelScope.launch {
        userPreference.storeUserName(username)
    }

    private fun storeDisplayName(displayName: String) = viewModelScope.launch {
        userPreference.storeDisplayName(displayName)
    }

    private fun storeAbout(about: String) = viewModelScope.launch {
        userPreference.storeAbout(about)
    }

    private fun storeAccessToken(accessToken: String) = viewModelScope.launch {
        userPreference.storeAccessToken(accessToken)
    }

    private fun storeRefreshToken(refreshToken: String) = viewModelScope.launch {
        userPreference.storeRefreshToken(refreshToken)
    }

    private fun storeId(id: String) = viewModelScope.launch {
        userPreference.storeId(id)
    }

}