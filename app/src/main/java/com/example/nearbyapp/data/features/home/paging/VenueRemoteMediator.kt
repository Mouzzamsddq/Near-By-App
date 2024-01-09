package com.example.nearbyapp.data.features.home.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.nearbyapp.base.NearByDb
import com.example.nearbyapp.data.features.home.local.entity.Venue
import com.example.nearbyapp.data.features.home.local.entity.VenueRemoteKeys
import com.example.nearbyapp.data.features.home.remote.api.HomeApiService
import com.example.nearbyapp.utils.LatLng

@OptIn(ExperimentalPagingApi::class)
class VenueRemoteMediator(
    private val homeApiService: HomeApiService,
    private val venueDatabase: NearByDb,
    private val userCurrentLocation: LatLng,
    private val range: String,
) : RemoteMediator<Int, Venue>() {

    private val venueDao = venueDatabase.venueDao()
    private val venueRemoteKeysDao = venueDatabase.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Venue>): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null,
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null,
                        )
                    Log.d("kkk", "next key : $nextPage")
                    nextPage
                }
            }

//            val response = homeApiService.getVenues(
//                perPage = 10,
//                page = 1,
//                clientId = BuildConfig.CLIENT_ID,
//                latitude = 37.3346433,
//                longitude = -122.0089717,
//                range = "12mi",
//                query = "",
//            )
            val response = homeApiService.getVenues(
                "https://api.seatgeek.com/2/venues?per_page=10&page=$currentPage&client_id=Mzg0OTc0Njl8MTcwMDgxMTg5NC44MDk2NjY5&lat=${userCurrentLocation.lat}&lon=${userCurrentLocation.lng}&range=12mi&q=ub",
            )
            val endOfPaginationReached = response.meta?.total == currentPage

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached || currentPage == 4) null else currentPage + 1

            venueDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    venueDao.deleteVenues()
                    venueRemoteKeysDao.deleteAllRemoteKeys()
                }

                venueDao.addVenues(response.venues?.map { it.toVenueEntity() } ?: emptyList())
                val keys = response.venues?.map { venue ->
                    VenueRemoteKeys(
                        id = venue.id,
                        prevKey = prevPage,
                        nextKey = nextPage,
                    )
                } ?: emptyList()
                venueRemoteKeysDao.addAllRemoteKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Venue>,
    ): VenueRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                venueRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Venue>,
    ): VenueRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { venue ->
                venueRemoteKeysDao.getRemoteKeys(id = venue.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Venue>,
    ): VenueRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { venue ->
                Log.d("kkk", "next key id : ${venue.id}")
                venueRemoteKeysDao.getRemoteKeys(id = venue.id)
            }
    }
}
