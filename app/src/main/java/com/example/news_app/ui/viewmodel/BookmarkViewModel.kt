package com.example.news_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.news_app.data.model.NewsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookmarkViewModel() : ViewModel() {
    private val _bookmarkedNews = MutableStateFlow<List<NewsItem>>(emptyList())
    val bookmarks: StateFlow<List<NewsItem>> = this._bookmarkedNews

    fun addBookmark(newsItem: NewsItem) {
        val updatedItem = newsItem.copy(isBookmarked = true)
        val currentBookmarks = _bookmarkedNews.value
        if (!currentBookmarks.contains(updatedItem)) {
            _bookmarkedNews.value = currentBookmarks + updatedItem
        }
    }

    fun removeBookmark(newsItem: NewsItem) {
        val currentBookmarks = _bookmarkedNews.value
        _bookmarkedNews.value = currentBookmarks.filter { it.url != newsItem.url }
    }

}