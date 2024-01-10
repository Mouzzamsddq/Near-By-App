package com.example.nearbyapp.data.features.home

import com.example.nearbyapp.data.features.home.local.LocalHomeDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val localHomeDataSource: LocalHomeDataSource,
) {
    fun getVenueListBasedOnSearchQuery(searchQuery: String) =
        localHomeDataSource.getVenuesListBasedOnNameSearch(searchQuery = searchQuery)
}
