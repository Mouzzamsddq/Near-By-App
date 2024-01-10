package com.example.nearbyapp.data.features.home

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.nearbyapp.base.NearByDb
import com.example.nearbyapp.data.features.home.paging.VenueRemoteMediator
import com.example.nearbyapp.data.features.home.remote.api.HomeApiService
import com.example.nearbyapp.utils.LatLng
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeApiService: HomeApiService,
    private val venueDatabase: NearByDb,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getVenues(
        userLocation: LatLng,
        distance: Int,
        searchQuery: String?,
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
        ),
        remoteMediator = VenueRemoteMediator(
            homeApiService = homeApiService,
            venueDatabase = venueDatabase,
            userCurrentLocation = userLocation,
            distance = distance,
        ),
        initialKey = 1,
        pagingSourceFactory = { venueDatabase.venueDao().getVenues(query = searchQuery) },
    ).liveData
}
