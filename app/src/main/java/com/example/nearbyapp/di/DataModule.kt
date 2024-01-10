package com.example.nearbyapp.di

import android.app.Application
import com.example.nearbyapp.base.NearByDb
import com.example.nearbyapp.data.features.home.local.dao.RemoteKeysDao
import com.example.nearbyapp.data.features.home.local.dao.VenueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDatabase(application: Application) = NearByDb.getDatabase(application)

    @Provides
    fun venueDao(db: NearByDb): VenueDao = db.venueDao()

    @Provides
    fun remoteKeysDao(db: NearByDb): RemoteKeysDao = db.remoteKeyDao()
}
