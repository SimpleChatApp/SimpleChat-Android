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
import com.kagan.chatapp.models.*
import com.kagan.chatapp.repositories.LoginRepository
import com.kagan.chatapp.utils.UserEvent
import io.sentry.Sentry
import io.sentry.SentryLevel
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "LoginViewModel"

class LoginViewModel @ViewModelInject
constructor(
    private val repository: LoginRepository,
    private val gson: Gson
) : ViewModel() {

    private val _dataState = MutableLiveData<UserEvent>()
    val dataState: LiveData<UserEvent> = _dataState

    private val _loginResult = MutableLiveData<UserAuthenticationVM>()
    val loginResult: LiveData<UserAuthenticationVM> = _loginResult

    private val _loginFailure = MutableLiveData<Boolean>()
    val loginFailure: LiveData<Boolean> = _loginFailure

    private val _loginError = MutableLiveData<APIResultVM>()
    val loginErrors: LiveData<APIResultVM> = _loginError

    private val _registerResultWithRec = MutableLiveData<APIResultWithRecVM<UserAuthenticationVM>>()
    val registerResultWithRecVM: LiveData<APIResultWithRecVM<UserAuthenticationVM>> =
        _registerResultWithRec

    private val _registerErrors = MutableLiveData<APIResultVM>()
    val registerErrors: LiveData<APIResultVM> = _registerErrors

    private val _registerOnFailure = MutableLiveData<Boolean>()
    val registerOnFailure: LiveData<Boolean> = _registerOnFailure

    private val _logoutSuccess = MutableLiveData<TokenRefreshVM>()
    val logoutSuccess: LiveData<TokenRefreshVM> = _logoutSuccess

    private val _logoutError = MutableLiveData<APIResultVM>()
    val logoutError: LiveData<APIResultVM> = _logoutError

    private val _logoutRefreshTokenClean = MutableLiveData<Boolean>()
    val logoutRefreshTokenClean: LiveData<Boolean> = _logoutRefreshTokenClean

    private val _logoutFailure = MutableLiveData<Boolean>()
    val logoutFailure: LiveData<Boolean> = _logoutFailure

    private var _logoutRequestCount = 0

    private val _isValid = MutableLiveData<Boolean>()
    val isValid: LiveData<Boolean> = _isValid

    fun checkTokenIsValid(accessToken: String) = viewModelScope.launch {
        _dataState.value = UserEvent.Loading
        val response = repository.checkTokenIsValid(accessToken)

        when (response.code()) {
            200 -> {
                _isValid.value = true
                _dataState.value = UserEvent.Valid
            }
            401 -> {
                _isValid.value = false
                _dataState.value = UserEvent.NotValid
            }
            500 -> {
                // Todo something happened
            }
        }
    }

    fun login(loginRequestVM: LoginUserRequestVM) {
        val call = repository.login(loginRequestVM.requestBody)

        call.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                _loginFailure.value = true
                Log.d("retrofit", "call failed")
                Sentry.captureMessage(t.toString(), SentryLevel.ERROR)

                loginRequestVM.requestCount++
                if (loginRequestVM.requestCount < 3) {
                    login(loginRequestVM)
                } else {
                    // todo Something happened
                    _loginFailure.value = false
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    400 -> {
                        loginResponseFailed(response)
                    }
                    404 -> {
                        loginResponseFailed(response)
                    }
                    409 -> {
                        loginResponseFailed(response)
                    }
                    200 -> {
                        try {
                            val body = response.body()
                            val user = parseJsonToVM(body!!, UserAuthenticationVM::class.java)

                            _loginResult.value = user
                        } catch (e: Exception) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        }
                    }
                    500 -> {
                        // todo something happened
                    }
                }
            }
        })
    }

    fun logout(authorization: String) {
        val call = repository.logout(authorization)

        call.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                _logoutFailure.value = true
                Log.d("retrofit", "call failed")
                Sentry.captureMessage(t.toString(), SentryLevel.ERROR)

                _logoutRequestCount++
                if (_logoutRequestCount < 3) {
                    logout(authorization)
                } else {
                    // todo Something happened
                    _logoutFailure.value = false
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when (response.code()) {
                    200 -> {
                        try {
                            val body = response.body()
                            _logoutSuccess.value = parseJsonToVM(body!!, TokenRefreshVM::class.java)
                        } catch (e: Exception) {
                            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                        }
                    }
                    204 -> {
                        _logoutRefreshTokenClean.value = true
                        Log.d(TAG, "204: ")
                    }
                    400 -> {
                        logoutResponseFailed(response)
                        Log.d(TAG, "400: ")
                    }
                    409 -> {
                        logoutResponseFailed(response)
                        Log.d(TAG, "onResponse: 409")
                    }
                    500 -> {
                        // todo something happened
                    }
                }
            }
        })
    }

    fun register(registerUserVM: RegisterUserRequestVM) {
        val call = repository.register(registerUserVM.requestBody)

        call.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                _registerOnFailure.value = true
                Log.d("retrofit", "call failed")
                Sentry.captureMessage(t.toString(), SentryLevel.ERROR)

                registerUserVM.requestCount++
                if (registerUserVM.requestCount < 3) {
                    register(registerUserVM)
                } else {
                    // todo Something happened
                    _registerOnFailure.value = false
                }
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (response == null) {
                    Sentry.captureMessage("$call response is null", SentryLevel.ERROR)
                } else {
                    when (response.code()) {
                        400 -> {
                            registerResponseFailed(response)
                        }
                        201 -> {
                            try {
                                val body = response.body()
                                val apiResultWithRecVMType = object :
                                    TypeToken<APIResultWithRecVM<UserAuthenticationVM>>() {}.type
                                val rec = gson.fromJson<APIResultWithRecVM<UserAuthenticationVM>>(
                                    gson.toJson(body),
                                    apiResultWithRecVMType
                                )

                                _registerResultWithRec.value = rec

                            } catch (e: JsonSyntaxException) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            } catch (e: JSONException) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            } catch (e: Exception) {
                                Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
                            }
                        }
                        422 -> {
                            registerResponseFailed(response)
                        }
                        500 -> {
                            // todo something happened
                        }
                    }
                }
            }
        })
    }

    private fun loginResponseFailed(response: Response<JsonElement>) {
        _loginError.value = convertResponseToVM(response)
    }

    private fun logoutResponseFailed(response: Response<JsonElement>) {
        _logoutError.value = convertResponseToVM(response)
    }

    private fun registerResponseFailed(response: Response<JsonElement>) {
        _registerErrors.value = convertResponseToVM(response)
    }

    private fun convertResponseToVM(response: Response<JsonElement>): APIResultVM? {
        var convert: APIResultVM? = null
        try {
            val errors = response.errorBody()?.string()
            convert = gson.fromJson(
                errors,
                APIResultVM::class.java
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: Exception) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return convert
    }

    private fun <T : Any> parseJsonToVM(body: JsonElement, clazz: Class<T>): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                gson.toJson(body),
                clazz
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }

    fun clearResult() {
        _loginResult.value = null
    }

    fun clearFailure() {
        _loginFailure.value = null
    }

    fun clearError() {
        _loginError.value = null
    }

    fun clearLogoutRefreshToken() {
        _logoutRefreshTokenClean.value = false
    }

    fun setState(state: UserEvent) {
        _dataState.value = state
    }
}