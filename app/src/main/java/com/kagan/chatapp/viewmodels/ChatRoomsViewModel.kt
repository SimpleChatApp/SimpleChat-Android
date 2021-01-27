package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kagan.chatapp.models.APIResultVM
import com.kagan.chatapp.models.APIResultWithRecVM
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.models.chatrooms.ChatRoomUpdateVM
import com.kagan.chatapp.models.chatrooms.ChatRoomVM
import com.kagan.chatapp.models.chatrooms.MessageVM
import com.kagan.chatapp.repositories.ChatRoomRepository
import com.kagan.chatapp.utils.States
import io.sentry.Sentry
import io.sentry.SentryLevel
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*

class ChatRoomsViewModel
@ViewModelInject
constructor(
    private val chatRoomsRepository: ChatRoomRepository,
    private val gson: Gson
) : ViewModel() {

    private val _state = MutableLiveData<States<*>>()
    val state: LiveData<States<*>> = _state

    private val _postState = MutableLiveData<States<*>>()
    val postState: LiveData<States<*>> = _postState

    private val _chatRoomUsers = MutableLiveData<States<*>>()
    val chatRoomUsers: LiveData<States<*>> = _chatRoomUsers

    private val _chatRoom = MutableLiveData<States<ChatRoomVM>>()
    val chatRoom: LiveData<States<ChatRoomVM>> = _chatRoom

    private val _chatRoomFailed = MutableLiveData<States<APIResultVM>>()
    val chatRoomFailed: LiveData<States<APIResultVM>> = _chatRoomFailed

    private val _putState = MutableLiveData<States<APIResultVM>>()
    val putState: LiveData<States<APIResultVM>> = _putState

    private val _deleteState = MutableLiveData<States<*>>()
    val deleteState: LiveData<States<*>> = _deleteState

    private val _chatRoomMessage = MutableLiveData<States<List<MessageVM>>>()
    val chatRoomMessage: LiveData<States<List<MessageVM>>> = _chatRoomMessage

    fun getChatRooms(auth: String) {
        _state.value = States.Loading
        val call = chatRoomsRepository.getChatRooms(auth)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        val chatRoomsType = object : TypeToken<List<ChatRoomVM>>() {}.type
                        _state.value = States.Success(
                            parseJsonToVM<List<ChatRoomVM>>(
                                response.body().toString(),
                                chatRoomsType
                            )
                        )
                    }
                    400 -> {
                        _state.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _state.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        Log.d(TAG, "onResponse: 500")
                        //todo something happened
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun postChatRooms(auth: String, chatRooms: AddVM) {
        _postState.value = States.Loading

        val call = chatRoomsRepository.postChatRooms(auth, chatRooms)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        _postState.value = States.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                APIResultVM::class.java
                            )
                        )
                    }
                    400 -> {
                        _postState.value =
                            States.Error(
                                parseJsonToVM(
                                    response.errorBody()?.string()!!,
                                    APIResultVM::class.java
                                )
                            )
                    }
                    404 -> {
                        _postState.value =
                            States.Error(
                                parseJsonToVM(
                                    response.errorBody()?.string()!!,
                                    APIResultVM::class.java
                                )
                            )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatRoomUsers(auth: String, id: UUID) {
        _chatRoomUsers.value = States.Loading

        val call = chatRoomsRepository.getChatRoomUsers(auth, id)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        _chatRoomUsers.value = States.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                APIResultWithRecVM::class.java
                            )
                            // todo i don't understand what type return
                        )
                    }
                    400 -> {
                        _chatRoomUsers.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _chatRoomUsers.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatRoom(auth: String, id: UUID) {
        _chatRoom.value = States.Loading

        val call = chatRoomsRepository.getChatRoom(auth, id)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {

                    200 -> {
                        _chatRoom.value = States.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                ChatRoomVM::class.java
                            )
                        )
                    }
                    400 -> {
                        _chatRoomFailed.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _chatRoomUsers.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun putChatRoom(auth: String, id: UUID, chatRoom: ChatRoomUpdateVM) {
        _putState.value = States.Loading
        val call = chatRoomsRepository.putChatRoom(auth, id, chatRoom)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {

                    200 -> {
                        _putState.value = States.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                APIResultVM::class.java
                            )
                        )
                    }
                    400 -> {
                        _putState.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _putState.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun deleteChatRoom(auth: String, id: UUID) {
        _deleteState.value = States.Loading
        val call = chatRoomsRepository.deleteChatRoom(auth, id)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    204 -> {
                        _deleteState.value = States.Success(Any())
                    }
                    400 -> {
                        _deleteState.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    404 -> {
                        _deleteState.value = States.Error(
                            parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java
                            )
                        )
                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatRoomMessages(auth: String, id: UUID) {
        _chatRoomMessage.value = States.Loading

        val call = chatRoomsRepository.getChatRoomMessages(auth, id)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        val messageListType = object : TypeToken<List<MessageVM>>() {}.type
                        _chatRoomMessage.value = States.Success(
                            parseJsonToVM(
                                response.body().toString(),
                                messageListType
                            )
                        )
                    }
                    400 -> {

                    }
                    404 -> {

                    }
                    500 -> {
                        TODO("Something happened")
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun <T : Any> parseJsonToVM(body: String, types: Type): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                body,
                types
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }

    private fun <T : Any> parseJsonToVM(
        body: String,
        clazz: Class<T>
    ): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                body,
                clazz
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }
}