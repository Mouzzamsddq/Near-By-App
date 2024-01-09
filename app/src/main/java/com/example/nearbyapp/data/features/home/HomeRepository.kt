package com.example.nearbyapp.data.features.home

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.nearbyapp.base.NearByDb
import com.example.nearbyapp.data.features.home.paging.VenueRemoteMediator
import com.example.nearbyapp.data.features.home.remote.api.HomeApiService
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeApiService: HomeApiService,
    private val venueDatabase: NearByDb,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getVenues() = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
        ),
        remoteMediator = VenueRemoteMediator(homeApiService, venueDatabase),
        pagingSourceFactory = { venueDatabase.venueDao().getVenues() },
    ).flow
}
