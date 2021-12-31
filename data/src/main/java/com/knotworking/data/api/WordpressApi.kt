package com.knotworking.data.api

import com.knotworking.data.Networking
import com.knotworking.data.api.models.AuthRequest
import com.knotworking.data.api.models.PostLocationRequest
import com.knotworking.data.api.models.TokenResponse
import retrofit2.Response
import retrofit2.http.*

interface WordpressApi {

    @POST("jwt-auth/v1/token")
    suspend fun getNewToken(
        @Body body: AuthRequest
    ): Response<TokenResponse>

    @PUT("jwt-auth/v1/tear/map-config")
    @Headers("${Networking.AUTHORIZATION_HEADER}: replace")
    suspend fun setCurrentLocation(
        @Body body: PostLocationRequest,
    ): Response<Void>
}