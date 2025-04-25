package com.example.news_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news_app.data.model.NewsItem

class BookmarkViewModel() : ViewModel() {
    private val _bookmarkedNews = MutableLiveData<List<NewsItem>>(emptyList())
    val bookmarks: LiveData<List<NewsItem>> = this._bookmarkedNews

    fun addBookmark(newsItem: NewsItem) {
        val currentBookmarks = _bookmarkedNews.value ?: emptyList()
        if (!currentBookmarks.contains(newsItem)) {
            _bookmarkedNews.value = currentBookmarks + newsItem
        }
    }

    fun removeBookmark(newsItem: NewsItem) {
        val currentBookmarks = _bookmarkedNews.value ?: emptyList()
        _bookmarkedNews.value = currentBookmarks - newsItem
    }
}