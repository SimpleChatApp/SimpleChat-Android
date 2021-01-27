package com.kagan.chatapp.api

import com.google.gson.JsonElement
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.models.chatrooms.ChatRoomUpdateVM
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ChatRoomsApi {

    @GET("chatrooms")
    fun getChatRooms(@Header("Authorization") auth: String): Call<JsonElement>

    @POST("chatrooms")
    fun postChatRooms(
        @Header("Authorization") auth: String,
        @Body chatRoom: AddVM
    ): Call<JsonElement>

    @GET("chatrooms/{id}/users")
    fun getChatRoomUsers(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>

    @GET("chatrooms/{id}")
    fun getChatRoom(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>

    @GET("chatrooms/{id}/messages")
    fun getChatRoomMessages(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>

    @PUT("chatrooms/{id}")
    fun putChatRoom(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID,
        @Body chatRoom: ChatRoomUpdateVM
    ): Call<JsonElement>

    @DELETE("chatrooms/{id}")
    fun deleteChatRoom(
        @Header("Authorization") auth: String,
        @Path("id") id: UUID
    ): Call<JsonElement>
}