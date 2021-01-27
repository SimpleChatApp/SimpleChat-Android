package com.kagan.chatapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kagan.chatapp.api.AuthenticationApi
import com.kagan.chatapp.api.ChatRoomsApi
import com.kagan.chatapp.api.MessageApi
import com.kagan.chatapp.api.UserApi
import com.kagan.chatapp.utils.Constants
import com.kagan.chatapp.utils.UnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
    }

    @Singleton
    @Provides
    fun provideAuthenticationService(retrofit: Retrofit.Builder): AuthenticationApi {
        return retrofit
            .build()
            .create(AuthenticationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit.Builder): UserApi {
        return retrofit.build()
            .create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideChatRoomsService(retrofit: Retrofit.Builder): ChatRoomsApi {
        return retrofit.build()
            .create(ChatRoomsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMessageService(retrofit: Retrofit.Builder): MessageApi {
        return retrofit.build().create(MessageApi::class.java)
    }
}