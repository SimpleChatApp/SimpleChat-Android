package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.AuthenticationApi
import com.kagan.chatapp.models.LoginRequestVM
import com.kagan.chatapp.models.RegisterUserVM

class LoginRepository constructor(
    private val authenticationApi: AuthenticationApi
) {

    fun login(loginRequestVM: LoginRequestVM) =
        authenticationApi.login(loginRequestVM)

    fun logout(authorization: String) = authenticationApi.logout("Bearer $authorization")
    fun register(registerUserVM: RegisterUserVM) = authenticationApi.register(registerUserVM)
    suspend fun checkTokenIsValid(accessToken: String) =
        authenticationApi.checkTokenIsValid("Bearer $accessToken")
}