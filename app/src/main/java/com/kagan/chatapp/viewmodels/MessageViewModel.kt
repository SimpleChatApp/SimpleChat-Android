package com.kagan.chatapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kagan.chatapp.models.chatrooms.MessageVM
import java.util.*

class MessageViewModel
@ViewModelInject
constructor() : ViewModel() {

    private val _messageList = MutableLiveData<List<MessageVM>>()

    fun getMessages(auth: String, id: UUID) {

    }

    fun putMessage(auth: String, id: UUID) {

    }

    fun deleteMessage(auth: String, id: UUID) {

    }
}