package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.UserUpdateVM
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface UserApi {

    @GET("users")
    fun getUsers(@Header("Authorization") accessToken: String): Call<JsonElement>

    @GET("users/{id}")
    fun getUser(
        @Path("id") id: UUID,
        @Header("Authorization") accessToken: String
    ): Call<JsonElement>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: UUID,
        @Body user: UserUpdateVM,
        @Header("Authorization") accessToken: String
    ): Call<JsonElement>
}