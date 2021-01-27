package com.kagan.chatapp.models

data class LoginRequestVM(
    val UserName: String,
    val Password: String
)

class LoginUserRequestVM {
    lateinit var requestBody: LoginRequestVM
    var requestCount: Int = 0
}