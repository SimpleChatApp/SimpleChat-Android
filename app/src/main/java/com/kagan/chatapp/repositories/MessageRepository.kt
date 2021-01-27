package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.MessageApi
import java.util.*

class MessageRepository
constructor(private val messageApi: MessageApi) {

    fun getMessages(auth: String, id: UUID) = messageApi.getMessages("Bearer $auth", id)
    fun putMessage(auth: String, id: UUID) = messageApi.putMessage("Bearer $auth", id)
    fun deleteMessage(auth: String, id: UUID) = messageApi.deleteMessage("Bearer $auth", id)
}