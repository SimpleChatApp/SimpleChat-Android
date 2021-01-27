package com.kagan.chatapp.di

import android.content.Context
import androidx.room.Room
import com.kagan.chatapp.db.UserDatabase
import com.kagan.chatapp.db.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UsersDao =
        userDatabase.userDao()

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "User_database"
        ).build()

}