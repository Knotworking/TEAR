package com.knotworking.data.api

import com.knotworking.data.api.models.AuthRequest
import com.knotworking.data.api.models.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WordpressApi {

    @POST("jwt-auth/v1/token")
    suspend fun getNewToken(
        @Body body: AuthRequest
    ): Response<TokenResponse>
}