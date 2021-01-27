package com.kagan.chatapp.models.chatrooms

import com.kagan.chatapp.models.BaseVM
import java.util.*

data class ChatRoomVM(
    override val Id: UUID,
    val Description: String,
    val IsMain: Boolean,
    val IsOneToOneChat: Boolean,
    val IsPrivate: Boolean,
    val Name: String,
    val Users: List<String>
) : BaseVM()
