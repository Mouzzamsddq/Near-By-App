package com.example.nearbyapp.data.features.home.local

import com.example.nearbyapp.data.features.home.local.dao.VenueDao
import javax.inject.Inject

class LocalHomeDataSource @Inject constructor(
    private val dao: VenueDao,
) {

    fun getVenuesListBasedOnNameSearch(searchQuery: String) =
        dao.getVenuesList(searchQuery = searchQuery)
}
