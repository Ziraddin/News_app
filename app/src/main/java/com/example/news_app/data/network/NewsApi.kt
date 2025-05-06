package com.example.news_app.data.network

import com.example.news_app.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything/")
    suspend fun getNews(
        @Query("q") query: String,
    ): Response<NewsResponse>

}