package com.example.news_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsItem(
    val author: String,
    val description: String,
    val title: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
    var isBookmarked: Boolean = false,
) : Parcelable


@Parcelize
data class NewsResponse(
    val articles: List<NewsItem>
) : Parcelable
