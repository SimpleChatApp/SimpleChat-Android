package com.kagan.chatapp.repositories

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.kagan.chatapp.api.ChatRoomsApi
import com.kagan.chatapp.models.APIResultErrorCodeVM
import com.kagan.chatapp.models.APIResultVM
import com.kagan.chatapp.models.BaseVM
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.models.chatrooms.ChatRoomUpdateVM
import com.kagan.chatapp.models.chatrooms.ChatRoomVM
import com.kagan.chatapp.utils.ParseJsonToVM
import com.kagan.chatapp.utils.States
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatRoomRepository
constructor(
    private val chatRoomsApi: ChatRoomsApi,
    private val parseJsonToVM: ParseJsonToVM,
    private val gson: Gson
) {

    fun getChatRooms(auth: String): States<*> {
        var result: States<*>
        val call = chatRoomsApi.getChatRooms("Bearer $auth")
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                result = processResponse<List<ChatRoomVM>>(response)
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun postChatRooms(auth: String, chatRooms: AddVM) =
        chatRoomsApi.postChatRooms("Bearer $auth", chatRooms)

    fun getChatRoomUsers(auth: String, id: UUID) = chatRoomsApi.getChatRoomUsers("Bearer $auth", id)
    fun getChatRoom(auth: String, id: UUID) = chatRoomsApi.getChatRoom("Bearer $auth", id)
    fun putChatRoom(auth: String, id: UUID, chatRoom: ChatRoomUpdateVM) =
        chatRoomsApi.putChatRoom("Bearer $auth", id, chatRoom)

    fun getChatRoomMessages(auth: String, id: UUID) =
        chatRoomsApi.getChatRoomMessages("Bearer $auth", id)

    fun deleteChatRoom(auth: String, id: UUID) = chatRoomsApi.deleteChatRoom("Bearer $auth", id)

    fun showProgress(): States.Loading {
        return States.Loading
    }

    fun <T : BaseVM> processResponse(response: Response<JsonElement>): States<*> {
        return when (response.code()) {
            200 -> {
                val resultType = object : TypeToken<List<T>>() {}.type
                States.Success(
                    parseJsonToVM.parseJsonToVM(
                        response.body().toString(),
                        resultType,
                        gson
                    )
                )
            }
            400 -> {
                States.Error(
                    parseJsonToVM.parseJsonToVM(
                        response.errorBody()?.string()!!,
                        APIResultVM::class.java,
                        gson
                    )
                )
            }
            404 -> {
                States.Error(
                    parseJsonToVM.parseJsonToVM(
                        response.errorBody()?.string()!!,
                        APIResultVM::class.java,
                        gson
                    )
                )
            }
            500 -> {
                //todo something happened
                // Sentry
                States.Error(APIResultVM(RecId = null, IsSuccessful = false,Errors = arrayListOf<APIResultErrorCodeVM>)(
                    APIResultErrorCodeVM(Field = )
                ))
            }
            else -> States.Loading
        }
    }

    fun <T : List<BaseVM>> processResponse(response: Response<JsonElement>): States<*> {
        return when (response.code()) {
            200 -> {
                val resultType = object : TypeToken<List<T>>() {}.type
                States.Success(
                    parseJsonToVM.parseJsonToVM(
                        response.body().toString(),
                        resultType,
                        gson
                    )
                )
            }
            400 -> {
                States.Error(
                    parseJsonToVM.parseJsonToVM(
                        response.errorBody()?.string()!!,
                        APIResultVM::class.java,
                        gson
                    )
                )
            }
            404 -> {
                States.Error(
                    parseJsonToVM.parseJsonToVM(
                        response.errorBody()?.string()!!,
                        APIResultVM::class.java,
                        gson
                    )
                )
            }
            500 -> {
                //todo something happened
                // Sentry
            }
            else -> States.Loading
        }
    }
}