package com.example.nearbyapp.di

import android.app.Application
import com.example.nearbyapp.base.LocalStorage
import com.example.nearbyapp.data.features.auth.repository.local.dao.UsersDao
import com.example.nearbyapp.data.features.favourites.repository.local.dao.FavBooksDao
import com.example.nearbyapp.base.BookShelfDb
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

    @Provides
    fun favBookDao(db: BookShelfDb): FavBooksDao = db.favBookDao()
}
