package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.ChatRoomsApi
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.models.chatrooms.ChatRoomUpdateVM
import java.util.*

class ChatRoomRepository
constructor(private val chatRoomsApi: ChatRoomsApi) {

    fun getChatRooms(auth: String) = chatRoomsApi.getChatRooms("Bearer $auth")
    fun postChatRooms(auth: String, chatRooms: AddVM) =
        chatRoomsApi.postChatRooms("Bearer $auth", chatRooms)

    fun getChatRoomUsers(auth: String, id: UUID) = chatRoomsApi.getChatRoomUsers("Bearer $auth", id)
    fun getChatRoom(auth: String, id: UUID) = chatRoomsApi.getChatRoom("Bearer $auth", id)
    fun putChatRoom(auth: String, id: UUID, chatRoom: ChatRoomUpdateVM) =
        chatRoomsApi.putChatRoom("Bearer $auth", id, chatRoom)

    fun getChatRoomMessages(auth: String, id: UUID) =
        chatRoomsApi.getChatRoomMessages("Bearer $auth", id)

    fun deleteChatRoom(auth: String, id: UUID) = chatRoomsApi.deleteChatRoom("Bearer $auth", id)
}