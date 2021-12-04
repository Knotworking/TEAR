package com.knotworking.data.api

import android.util.Log
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
            Log.v("TAG", "token retrieved: ${response.body()!!.token}")
            tokenDataSource.setToken(response.body()!!.token)
        }

        return response.isSuccessful
    }

    override suspend fun setCurrentLocation(location: Location) {
        TODO("not implemented")
    }
}