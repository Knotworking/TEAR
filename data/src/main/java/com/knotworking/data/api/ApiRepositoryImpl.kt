package com.knotworking.data.api

import com.knotworking.data.BuildConfig
import com.knotworking.data.api.models.AuthRequest
import com.knotworking.data.api.models.PostLocationRequest
import com.knotworking.data.location.LocationDataSource
import com.knotworking.data.location.marker.MarkerDataSource
import com.knotworking.domain.api.ApiRepository
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ApiRepositoryImpl(
    private val wordpressApi: WordpressApi,
    private val tokenDataSource: TokenDataSource,
    private val locationDataSource: LocationDataSource,
    private val markerDataSource: MarkerDataSource
) : ApiRepository {
    override suspend fun getNewToken(): Boolean {
        val response = wordpressApi.getNewToken(
            body = AuthRequest(
                username = BuildConfig.WORDPRESS_USERNAME,
                password = BuildConfig.WORDPRESS_PASSWORD
            )
        )

        if (response.isSuccessful) {
            tokenDataSource.setToken(response.body()!!.token)
        }

        return response.isSuccessful
    }

    override suspend fun postCurrentLocation(): Boolean {
        val location = locationDataSource.getLastTrailLocation() ?: return false
        val markerText = markerDataSource.getMarkerText()

        val pattern = "dd/MM/yyyy HH:mm O"
        val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
        val updatedAtFormatted = formatter.format(Instant.ofEpochSecond(location.updatedAtSeconds))

        val body = PostLocationRequest(
            location = arrayOf(location.longitude, location.latitude),
            markerText = markerText,
            updatedAt = updatedAtFormatted
        )
        val response = wordpressApi.setCurrentLocation(body = body)
        return response.isSuccessful
    }
}