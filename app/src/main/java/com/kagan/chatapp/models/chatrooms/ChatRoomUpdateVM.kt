package com.kagan.chatapp.models.chatrooms

data class ChatRoomUpdateVM(
    val Description: String,
    val IsMain: Boolean,
    val IsOneToOneChat: Boolean,
    val IsPrivate: Boolean,
    val Name: String,
    val Users: List<String>
)
