package com.kagan.chatapp.utils

import com.kagan.chatapp.models.APIResultVM

sealed class States<out T : Any> {
    object Loading : States<Nothing>()
    data class Success<T : Any>(val data: T) : States<T>()
    data class Error(val error: APIResultVM) : States<APIResultVM>()
}
