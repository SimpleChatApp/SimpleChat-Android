package com.kagan.chatapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.UserVM
import com.kagan.chatapp.repositories.UserDBRepository
import com.kagan.chatapp.utils.States
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DatabaseViewModel
@ViewModelInject
constructor(private val userDBRepository: UserDBRepository) : ViewModel() {

    private val _usersState = MutableLiveData<States<List<UsersEntity>>>()
    val usersStates: LiveData<States<List<UsersEntity>>> = _usersState

    fun insertUser(user: UsersEntity) = viewModelScope.launch(IO) {
        userDBRepository.insertUser(user)
    }

    fun insertUsers(users: List<UsersEntity>) = viewModelScope.launch(IO) {
        userDBRepository.insertUsers(users)
    }

    fun getUsers() = viewModelScope.launch(IO) {
        withContext(Main) {
            _usersState.value = States.Loading
        }
        val users = userDBRepository.getUsers()

        withContext(Main) {
            _usersState.value = States.Success(users)
        }

    }

    fun deleteUsers(id: String) = viewModelScope.launch(IO) {
        userDBRepository.deleteUsers(id)
    }
}