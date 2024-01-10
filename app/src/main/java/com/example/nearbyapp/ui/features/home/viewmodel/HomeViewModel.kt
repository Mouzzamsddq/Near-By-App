package com.example.nearbyapp.ui.features.home.viewmodel

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
import com.example.nearbyapp.data.features.home.HomeRepository
import com.example.nearbyapp.data.features.home.paging.VenueRemoteMediator
import com.example.nearbyapp.data.features.home.remote.api.HomeApiService
import com.example.nearbyapp.utils.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeApiService: HomeApiService,
    private val venueDatabase: NearByDb,
    private val homeRepo: HomeRepository,
) : ViewModel() {

    private val _userLocationLd = MutableLiveData<LatLng>()
    private val _distanceFilterLd = MutableLiveData(3)
    private val _searchQuery = MutableLiveData<String?>(null)

    private val combinedLiveData = MediatorLiveData<Triple<LatLng, Int, String?>>().apply {
        addSource(_userLocationLd) { location ->
            location?.let {
                _distanceFilterLd.value?.let {
                    value = Triple(location, it, _searchQuery.value)
                }
            }
        }

        addSource(_distanceFilterLd) { distanceFilter ->
            distanceFilter?.let {
                _userLocationLd.value?.let { latlng ->
                    value = Triple(latlng, it, _searchQuery.value)
                }
            }
        }
        addSource(_searchQuery) { query ->
            _distanceFilterLd.value?.let {
                _userLocationLd.value?.let { latlng ->
                    value = Triple(latlng, it, query)
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
            pagingSourceFactory = { venueDatabase.venueDao().getVenues(query = it.third) },
        ).liveData.cachedIn(viewModelScope)
    }

    fun updatedUserLocation(userLocation: LatLng) {
        _userLocationLd.value = userLocation
    }

    fun changeDistanceFilter(distance: Int) {
        _distanceFilterLd.value = distance
    }

    fun searchByVenueName(searchQuery: String?) {
        _searchQuery.value = searchQuery
    }
}
