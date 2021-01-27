package com.kagan.chatapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kagan.chatapp.db.dao.UsersDao
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.db.typeconverters.UUIDConverter

@Database(entities = [UsersEntity::class], version = 1)
@TypeConverters(UUIDConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UsersDao
}