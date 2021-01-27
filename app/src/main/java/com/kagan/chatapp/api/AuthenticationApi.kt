package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.LoginRequestVM
import com.kagan.chatapp.models.RegisterUserVM
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("tokens/create")
    fun login(@Body loginRequestVM: LoginRequestVM): Call<JsonElement>

    @POST("tokens/revoke")
    fun logout(@Header("Authorization") token: String): Call<JsonElement>

    @POST("authentications/register")
    fun register(@Body registerUserVM: RegisterUserVM): Call<JsonElement>

    @POST("tokens/validate")
    suspend fun checkTokenIsValid(@Header("Authorization") accessToken: String): Response<Unit>
}