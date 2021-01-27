package com.kagan.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kagan.chatapp.models.UserAuthenticationVM

class SharedViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<UserAuthenticationVM>()
    val currentUser: LiveData<UserAuthenticationVM> = _currentUser

    fun postValue(loginResult: UserAuthenticationVM) {
        _currentUser.value = loginResult
    }
}