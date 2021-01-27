package com.kagan.chatapp.models

import java.util.*

data class APIResultVM(
    val RecId: UUID? = null,
    val IsSuccessful: Boolean,
    val Errors: List<APIResultErrorCodeVM>? = null
)