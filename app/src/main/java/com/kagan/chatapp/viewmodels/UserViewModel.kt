package com.kagan.chatapp.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.kagan.chatapp.db.mappers.NetworkMapper
import com.kagan.chatapp.models.*
import com.kagan.chatapp.repositories.UserDBRepository
import com.kagan.chatapp.repositories.UserRepository
import com.kagan.chatapp.utils.ParseJsonToVM
import com.kagan.chatapp.utils.States
import com.kagan.chatapp.utils.UserEvent
import io.sentry.Sentry
import io.sentry.SentryLevel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*

class UserViewModel @ViewModelInject
constructor(
    private val userRepository: UserRepository,
    private val gson: Gson,
    private val networkMapper: NetworkMapper,
    private val parseJsonToVM: ParseJsonToVM,
    private val userDBRepository: UserDBRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<UserEvent>()
    val isLoading: LiveData<UserEvent> = _isLoading

    private val _user = MutableLiveData<APIResultWithRecVM<UserVM>>()
    val user: LiveData<APIResultWithRecVM<UserVM>> = _user

    private val _usersState = MutableLiveData<States<List<UserVM>>>()
    val usersStates: LiveData<States<List<UserVM>>> = _usersState

    private val _usersStateError = MutableLiveData<States<APIResultVM>>()
    val usersStateError: LiveData<States<APIResultVM>> = _usersStateError

    init {
        _isLoading.value = UserEvent.Loading
    }

    fun getUsers(auth: String) {
        _usersState.value = States.Loading
        val call = userRepository.getUsers(auth)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        val usersListType = object : TypeToken<List<UserVM>>() {}.type
                        val users = parseJson(
                            response.body().toString(),
                            usersListType,
                            gson
                        )
                        val usersEntity = networkMapper.mapListDaoToEntity(users)
                        runBlocking {
                            userDBRepository.deleteTable()
                            usersEntity.forEach {
                                userDBRepository.insertUser(it)
                            }
                        }
                    }
                    400 -> {
                        _usersStateError.value = States.Error(
                            parseJsonToVM.parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java,
                                gson
                            )
                        )
                    }
                    404 -> {
                        _usersStateError.value = States.Error(
                            parseJsonToVM.parseJsonToVM(
                                response.errorBody()?.string()!!,
                                APIResultVM::class.java,
                                gson
                            )
                        )
                    }
                    500 -> {
                        // todo something happened
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getUser(currentUser: UserAuthenticationVM) {
        Log.d(TAG, "getUser: running")
        val call = userRepository.getUser(currentUser.Id, currentUser.TokenData.AccessToken)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        try {
                            val body = response.body()
                            val userResultType =
                                object : TypeToken<APIResultWithRecVM<UserVM>>() {}.type
                            val recUser = gson
                                .fromJson<APIResultWithRecVM<UserVM>>(
                                    gson.toJson(body),
                                    userResultType
                                )

                            _user.value = recUser
                            _isLoading.value = UserEvent.Valid

                        } catch (e: JsonSyntaxException) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        } catch (
                            e: JSONException
                        ) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        } catch (e: Exception) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateUser(id: UUID, user: UserUpdateVM) {

    }

    private fun parseJson(body: String, type: Type, gson: Gson): List<UserVM> {
        var parse: List<UserVM>? = null
        try {
            parse = gson.fromJson(
                body,
                type
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }
}