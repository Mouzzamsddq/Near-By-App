package com.example.bookshelfapp.di

import android.app.Application
import com.example.bookshelfapp.base.LocalStorage
import com.example.bookshelfapp.data.features.auth.repository.local.dao.UsersDao
import com.smrize.app.data.db.BookShelfDb
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

    @Provides
    fun provideDatabase(application: Application) = BookShelfDb.getDatabase(application)

    @Provides
    fun usersDao(db: BookShelfDb): UsersDao = db.usersDao()
}
