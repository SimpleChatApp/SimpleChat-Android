package com.kagan.chatapp.models

import java.util.*

data class UserVM(
    override val Id: UUID,
    val UserName: String,
    val IsAccountLocked: Boolean,
    val DisplayName: String,
    val About: String
) : BaseVM()