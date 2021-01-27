package com.kagan.chatapp.models.chatrooms

data class AddVM(
    val Description: String,
    val IsMain: Boolean,
    val IsOneToOneChat: Boolean,
    val IsPrivate: Boolean,
    val Name: String,
    val Users: List<String>
)