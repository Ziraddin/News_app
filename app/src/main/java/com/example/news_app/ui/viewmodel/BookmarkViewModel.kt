package com.example.news_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.news_app.data.local.SharedPreferences
import com.example.news_app.data.model.NewsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookmarkViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _bookmarkedNews = MutableStateFlow(sharedPreferences.loadBookmarks())
    val bookmarks: StateFlow<List<NewsItem>> = this._bookmarkedNews

    fun addBookmark(newsItem: NewsItem) {
        val updatedItem = newsItem.copy(isBookmarked = true)
        val currentBookmarks = _bookmarkedNews.value
        val alreadyBookmarked = currentBookmarks.any { it.url == updatedItem.url }

        if (!alreadyBookmarked) {
            _bookmarkedNews.value = currentBookmarks + updatedItem
            sharedPreferences.saveBookmarks(_bookmarkedNews.value)
        }
    }

    fun removeBookmark(newsItem: NewsItem) {
        val currentBookmarks = _bookmarkedNews.value
        _bookmarkedNews.value = currentBookmarks.filter { it.url != newsItem.url }
        sharedPreferences.saveBookmarks(_bookmarkedNews.value)
    }

}