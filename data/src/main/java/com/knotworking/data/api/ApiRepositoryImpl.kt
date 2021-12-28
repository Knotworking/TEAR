package com.knotworking.data.api

import com.knotworking.data.BuildConfig
import com.knotworking.data.api.models.AuthRequest
import com.knotworking.domain.api.ApiRepository
import com.knotworking.domain.location.Location

class ApiRepositoryImpl(
    private val wordpressApi: WordpressApi,
    private val tokenDataSource: TokenDataSource
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

    override suspend fun setCurrentLocation(location: Location): Boolean {
        // In the future send a request object rather than an array
        val array = arrayOf(location.longitude, location.latitude)
        val response = wordpressApi.setCurrentLocation(body = array)
        return response.isSuccessful
    }
}