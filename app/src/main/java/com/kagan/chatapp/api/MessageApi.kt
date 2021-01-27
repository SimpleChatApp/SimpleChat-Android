package com.kagan.chatapp.api

import retrofit2.http.*
import java.util.*

interface MessageApi {

    @GET("messages/{id}")
    fun getMessages(@Header("Authorization") auth: String, @Path("id") id: UUID)

    @PUT("messages/{id}")
    fun putMessage(@Header("Authorization") auth: String, @Path("id") id: UUID)

    @DELETE("messages/{id}")
    fun deleteMessage(@Header("Authorization") auth: String, @Path("id") id: UUID)

}