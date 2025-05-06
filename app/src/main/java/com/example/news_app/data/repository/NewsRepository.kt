package com.example.news_app.data.repository

import com.example.news_app.data.network.RetrofitHelper
import com.example.news_app.data.model.NewsResponse
import com.example.news_app.data.network.NewsApi
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class NewsRepository(private val api: NewsApi = RetrofitHelper.instance) {

    suspend fun getNews(query: String): Response<NewsResponse> {
        return api.getNews(query)
    }
}

fun main() {
    runBlocking {
        val repository = NewsRepository()
        val response = repository.getNews("tesla")
        if (response.isSuccessful) {
            val searchResponse = response.body()
            searchResponse?.articles?.forEach {
                println("${it.title}, ${it.url}")
            }
        }
    }
}