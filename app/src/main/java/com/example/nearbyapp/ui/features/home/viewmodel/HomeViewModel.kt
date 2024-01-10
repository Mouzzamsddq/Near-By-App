package com.example.nearbyapp.ui.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.nearbyapp.base.NearByDb
import com.example.nearbyapp.data.features.home.paging.VenueRemoteMediator
import com.example.nearbyapp.data.features.home.remote.api.HomeApiService
import com.example.nearbyapp.utils.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeApiService: HomeApiService,
    private val venueDatabase: NearByDb,
) : ViewModel() {

    private val _userLocationLd = MutableLiveData<LatLng>()
    private val userLocationLd: LiveData<LatLng> = _userLocationLd
    private val _distanceFilterLd = MutableLiveData<Int>(3)
    private val distanceFilterLd: LiveData<Int> = _distanceFilterLd

    private val combinedLiveData = MediatorLiveData<Pair<LatLng, Int>>().apply {
        addSource(userLocationLd) { location ->
            location?.let {
                distanceFilterLd.value?.let {
                    value = Pair(location, it)
                }
            }
        }

        addSource(distanceFilterLd) { distanceFilter ->
            distanceFilter?.let {
                userLocationLd.value?.let { latlng ->
                    value = Pair(latlng, it)
                }
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    val nearByVenue = combinedLiveData.switchMap {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
            ),
            remoteMediator = VenueRemoteMediator(
                homeApiService,
                venueDatabase,
                userCurrentLocation = it.first,
                it.second,
            ),
            initialKey = 1,
            pagingSourceFactory = { venueDatabase.venueDao().getVenues() },
        ).liveData.cachedIn(viewModelScope)
    }

    fun updatedUserLocation(userLocation: LatLng) {
        _userLocationLd.value = userLocation
    }

    fun changeDistanceFilter(distance: Int) {
        _distanceFilterLd.value = distance
    }
}
