package com.example.nearbyapp.ui.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.nearbyapp.base.NearByDb
import com.example.nearbyapp.data.features.home.local.entity.Venue
import com.example.nearbyapp.data.features.home.paging.VenueRemoteMediator
import com.example.nearbyapp.data.features.home.remote.api.HomeApiService
import com.example.nearbyapp.utils.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeApiService: HomeApiService,
    private val venueDatabase: NearByDb,
) : ViewModel() {

    private val _locationFilter = MutableStateFlow(LatLng(0.0, 0.0))
    private val _rangeFilter = MutableStateFlow("")

    @OptIn(ExperimentalPagingApi::class)
    val nearByVenueFlow: Flow<PagingData<Venue>> = combine(
        _locationFilter,
        _rangeFilter,
    ) { userLocation, range ->
        Pair(userLocation, range)
    }.flatMapLatest { (userLocation, range) ->
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
            ),
            remoteMediator = VenueRemoteMediator(
                homeApiService,
                venueDatabase,
                userLocation,
                range,
            ),
            pagingSourceFactory = { venueDatabase.venueDao().getVenues() },
        ).flow.cachedIn(viewModelScope)
    }

    fun loadDataBasedOnUserCurrentLocation(latLng: LatLng) {
        _locationFilter.value = latLng
    }
}
