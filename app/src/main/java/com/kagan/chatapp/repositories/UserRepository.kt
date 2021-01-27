package com.kagan.chatapp.repositories

import com.kagan.chatapp.api.UserApi
import com.kagan.chatapp.models.UserUpdateVM
import java.util.*

class UserRepository constructor(
    private val userApi: UserApi
) {
    fun getUsers(accessToken: String) = userApi.getUsers("Bearer $accessToken")
    fun getUser(id: UUID, accessToken: String) = userApi.getUser(id, "Bearer $accessToken")
    fun updateUser(id: UUID, user: UserUpdateVM, accessToken: String) =
        userApi.updateUser(id, user, "Bearer $accessToken")
}