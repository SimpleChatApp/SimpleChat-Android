package com.kagan.chatapp.models

import java.util.*

data class UserAuthenticationVM(
    override val Id: UUID,
    val UserName: String,
    val DisplayName: String,
    val About: String,
    val TokenData: TokenCacheVM
) : BaseVM()

