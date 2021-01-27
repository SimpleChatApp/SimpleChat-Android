package com.kagan.chatapp.models

import java.util.*

data class UserListVM(
    override val Id: UUID,
    val UserName: String,
    val DisplayName: String
) : BaseVM()