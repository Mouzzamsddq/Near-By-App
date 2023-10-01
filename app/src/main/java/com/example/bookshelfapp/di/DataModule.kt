package com.example.bookshelfapp.di

import android.app.Application
import com.example.bookshelfapp.base.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideLocalStorage(application: Application) = LocalStorage(application)
}
