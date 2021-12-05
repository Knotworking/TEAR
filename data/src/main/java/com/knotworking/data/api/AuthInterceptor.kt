package com.knotworking.data.api

import com.knotworking.data.Networking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenDataSource: TokenDataSource) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val hasAuthHeader = chain.request().headers().get(Networking.AUTHORIZATION_HEADER) != null
        if (hasAuthHeader) {
            requestBuilder.removeHeader(Networking.AUTHORIZATION_HEADER)
            requestBuilder.addHeader(
                Networking.AUTHORIZATION_HEADER,
                "Bearer ${tokenDataSource.getToken()}"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}