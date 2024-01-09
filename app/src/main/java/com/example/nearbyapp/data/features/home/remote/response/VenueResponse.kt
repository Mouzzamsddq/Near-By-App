package com.example.nearbyapp.data.features.home.remote.response

import com.google.gson.annotations.SerializedName
import com.example.nearbyapp.data.features.home.local.entity.Venue as VenueEntity

data class VenueResponse(
    @SerializedName("meta")
    val meta: Meta? = null,
    @SerializedName("venues")
    val venues: List<Venue>? = null,
)

data class Venue(
    @SerializedName("access_method")
    val accessMethod: Any? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("capacity")
    val capacity: Int? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("display_location")
    val displayLocation: String? = null,
    @SerializedName("extended_address")
    val extendedAddress: String? = null,
    @SerializedName("has_upcoming_events")
    val hasUpcomingEvents: Boolean? = null,
    @SerializedName("id")
    val id: Long,
    @SerializedName("links")
    val links: List<Any?>? = null,
    @SerializedName("location")
    val location: Location? = null,
    @SerializedName("metro_code")
    val metroCode: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("name_v2")
    val nameV2: String? = null,
    @SerializedName("num_upcoming_events")
    val numUpcomingEvents: Int? = null,
    @SerializedName("popularity")
    val popularity: Int? = null,
    @SerializedName("postal_code")
    val postalCode: String? = null,
    @SerializedName("score")
    val score: Double? = null,
    @SerializedName("slug")
    val slug: String? = null,
    @SerializedName("state")
    val state: Any? = null,
    @SerializedName("stats")
    val stats: Stats? = null,
    @SerializedName("timezone")
    val timezone: String? = null,
    @SerializedName("url")
    val url: String? = null,
) {
    fun toVenueEntity() = VenueEntity(
        id = id ?: 0,
        name = nameV2 ?: "",
        url = url ?: "",
        displayLocation = displayLocation ?: "",
    )
}

data class Stats(
    @SerializedName("event_count")
    val eventCount: Int? = null,
)

data class Meta(
    @SerializedName("geolocation")
    val geolocation: Geolocation? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("per_page")
    val perPage: Int? = null,
    @SerializedName("took")
    val took: Int? = null,
    @SerializedName("total")
    val total: Int? = null,
)

data class Location(
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
)

data class Geolocation(
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("display_name")
    val displayName: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("metro_code")
    val metroCode: Any? = null,
    @SerializedName("postal_code")
    val postalCode: String? = null,
    @SerializedName("range")
    val range: String? = null,
    @SerializedName("state")
    val state: String? = null,
)
