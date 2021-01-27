package com.kagan.chatapp.di

import android.content.Context
import com.kagan.chatapp.datastore.CurrentUserPreference
import com.kagan.chatapp.datastore.TokenPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideTokenPreference(@ApplicationContext context: Context): TokenPreference =
        TokenPreference(context)

    @Singleton
    @Provides
    fun provideCurrentUserPreference(@ApplicationContext context: Context): CurrentUserPreference =
        CurrentUserPreference(context)
}