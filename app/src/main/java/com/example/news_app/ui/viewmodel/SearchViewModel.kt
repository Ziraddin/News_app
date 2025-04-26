package com.example.news_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_app.data.model.NewsItem
import com.example.news_app.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val result: List<NewsItem>) : SearchState()
    data class Error(val message: String) : SearchState()
}

class SearchViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = this._searchState

    fun searchNews(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading

            try {
                val response = repository.getNews(query)
                if (response.isSuccessful) {
                    val news = response.body()
                    news?.let {
                        _searchState.value = SearchState.Success(it.articles)
                    } ?: run {
                        _searchState.value = SearchState.Error("No data found.")
                    }
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error("Exception: ${e.localizedMessage}")
            }
        }
    }
}