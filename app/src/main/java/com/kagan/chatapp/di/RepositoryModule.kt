package com.kagan.chatapp.di

import com.kagan.chatapp.api.AuthenticationApi
import com.kagan.chatapp.api.UserApi
import com.kagan.chatapp.repositories.LoginRepository
import com.kagan.chatapp.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(authenticationApi: AuthenticationApi): LoginRepository =
        LoginRepository(authenticationApi)

    @Singleton
    @Provides
    fun provideUserRepository(userApi: UserApi): UserRepository = UserRepository(userApi)
}