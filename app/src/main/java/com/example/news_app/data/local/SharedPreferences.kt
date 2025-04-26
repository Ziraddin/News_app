package com.example.news_app.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.news_app.data.model.NewsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("bookmark_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveBookmarks(bookmarks: List<NewsItem>) {
        val json = gson.toJson(bookmarks)
        prefs.edit() { putString("bookmarks", json) }
    }

    fun loadBookmarks(): List<NewsItem> {
        val json = prefs.getString("bookmarks", null)
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<NewsItem>>() {}.type
        return gson.fromJson(json, type)
    }
}