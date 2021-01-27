package com.kagan.chatapp.db.dao

import androidx.room.*
import com.kagan.chatapp.db.entities.UsersEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UsersEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UsersEntity>)

    @Query("SELECT * FROM users")
    fun getUsers(): List<UsersEntity>

    @Query("DELETE FROM users WHERE Id= :id")
    suspend fun deleteUsers(id: String)

    @Query("DELETE FROM users")
    suspend fun deleteTable()
}