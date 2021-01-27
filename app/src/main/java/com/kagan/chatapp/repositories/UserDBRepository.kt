package com.kagan.chatapp.repositories

import com.kagan.chatapp.db.dao.UsersDao
import com.kagan.chatapp.db.entities.UsersEntity
import javax.inject.Inject

class UserDBRepository
@Inject
constructor(private val usersDao: UsersDao) {
    suspend fun insertUser(user: UsersEntity) = usersDao.insertUser(user)
    suspend fun insertUsers(users: List<UsersEntity>) = usersDao.insertUsers(users)

    fun getUsers() = usersDao.getUsers()

    suspend fun deleteUsers(id: String) = usersDao.deleteUsers(id)
    suspend fun deleteTable() = usersDao.deleteTable()
}