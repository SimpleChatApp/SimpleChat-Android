package com.kagan.chatapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kagan.chatapp.datastore.TokenPreference
import kotlinx.coroutines.launch

class TokenPreferenceViewModel
@ViewModelInject
constructor(
    private val tokenPreference: TokenPreference
) : ViewModel() {

    val accessToken = tokenPreference.accessTokenFlow.asLiveData()
    val refreshToken = tokenPreference.refreshTokenFlow.asLiveData()

    fun storeAccessToken(accessToken: String) = viewModelScope.launch {
        tokenPreference.storeAccessToken(accessToken)
    }

    fun storeRefreshToken(refreshToken: String) = viewModelScope.launch {
        tokenPreference.storeRefreshToken(refreshToken)
    }
}