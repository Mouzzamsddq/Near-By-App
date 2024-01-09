package com.example.nearbyapp.ui.features.home.viewmodel

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
    private val homeRepository: HomeRepository,
) : ViewModel() {


    private val detailsUrl = MutableLiveData<LatLng>()

    @OptIn(ExperimentalPagingApi::class)
    val nearByVenue = detailsUrl.switchMap {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
            ),
            remoteMediator = VenueRemoteMediator(
                homeApiService,
                venueDatabase,
                userCurrentLocation = it,
                "",
            ),
            initialKey = 1,
            pagingSourceFactory = { venueDatabase.venueDao().getVenues() },
        ).liveData.cachedIn(viewModelScope)
    }

    fun changeDetailUrl(userLocation: LatLng) {
        homeRepository.saveUserLocation(userLocation = userLocation)
        detailsUrl.value = userLocation
    }

    fun getSavedUserLocation() = homeRepository.getSavedUserLocation()
}
