package com.kagan.chatapp.utils

sealed class UserEvent {

    object Loading : UserEvent()
    object Valid : UserEvent()
    object NotValid : UserEvent()
}
