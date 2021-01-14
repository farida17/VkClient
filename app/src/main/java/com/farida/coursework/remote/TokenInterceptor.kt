package com.farida.coursework.remote

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response  {
        var request = chain.request()
        if (TokenHolder.tokenAccess.isNullOrBlank()) {
            throw IllegalArgumentException()
        }
        val url = request.url.newBuilder()
            .addQueryParameter(QUERY_TOKEN, TokenHolder.tokenAccess)
            .addQueryParameter(QUERY_API_VERSION, API_VERSION)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    private companion object {
        const val QUERY_TOKEN = "access_token"
        const val QUERY_API_VERSION = "v"
        const val API_VERSION = "5.124"
    }
}