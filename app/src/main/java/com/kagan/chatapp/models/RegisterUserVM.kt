package com.kagan.chatapp.models

data class RegisterUserVM(
    val username: String,
    val password: String,
    val confirmPassword: String,
    val displayName: String,
    val email: String,
    val about: String? = null,
)

class RegisterUserRequestVM {
    lateinit var requestBody: RegisterUserVM
    var requestCount: Int = 0
}