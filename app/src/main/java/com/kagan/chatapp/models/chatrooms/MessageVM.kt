package com.kagan.chatapp.models.chatrooms

import java.util.*

data class MessageVM(
    val Text: String,
    val ChatRoomId: UUID,
    val CreateDT: String,
    val UpdateDT: String? = null,
    val CreateBy: UUID,
    val UpdateBy: UUID
)