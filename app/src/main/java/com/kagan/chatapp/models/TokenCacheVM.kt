package com.kagan.chatapp.models

import java.util.*

data class TokenCacheVM(
    val AccessToken: String,
    val AccessTokenExpiryTime: Date,
    val RefreshToken: String,
    val RefreshTokenExpiryTime: Date
)
