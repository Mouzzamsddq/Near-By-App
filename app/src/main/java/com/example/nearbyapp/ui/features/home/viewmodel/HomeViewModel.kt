package com.example.nearbyapp.ui.features.home.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.room.util.query
import com.example.nearbyapp.data.features.home.HomeRepository
import com.example.nearbyapp.utils.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
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

    val nearByVenue = combinedLiveData.switchMap {
        homeRepository.getVenues(
            userLocation = it.first,
            distance = it.second,
            searchQuery = it.third,
        ).cachedIn(viewModelScope)
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
