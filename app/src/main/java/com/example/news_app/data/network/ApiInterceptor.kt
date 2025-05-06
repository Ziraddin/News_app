package com.example.news_app.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = "0e2fcd9688a742aa9cd692b971cc2099"
        val request = chain.request()
        val url = request.url
        val newUrl = url.newBuilder().addQueryParameter("apiKey", apiKey).build()
        val newRequest = request.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}