package com.kagan.chatapp.models

import java.util.*

data class APIResultWithRecVM<T>(
    val RecId: UUID? = null,
    val Rec: T? = null,
    val IsSuccessful: Boolean,
    val Errors: List<APIResultErrorCodeVM>? = null
)