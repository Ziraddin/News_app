package com.example.news_app.ui.fragment.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.news_app.R
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.FragmentBookmarkBinding
import com.example.news_app.ui.MainActivity
import com.example.news_app.ui.adapter.NewsRVadapter
import com.example.news_app.ui.fragment.search.SearchFragmentDirections
import com.example.news_app.ui.viewmodel.BookmarkViewModel

class BookmarkFragment : Fragment() {

    lateinit var binding: FragmentBookmarkBinding
    lateinit var bookmarkViewModel: BookmarkViewModel
    lateinit var rVadapter: NewsRVadapter
    private var data: List<NewsItem> = emptyList<NewsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        bookmarkViewModel = (activity as MainActivity).bookmarkViewModel
        rVadapter = NewsRVadapter(
            emptyList(),
            ::navigateToDetails,
            bookmarkAdd = ::bookmarkAdd,
            bookmarkRemove = ::bookmarkRemove
        )

        setUpRecyclerView()
        observeBookmarks()

        return binding.root
    }

    private fun bookmarkAdd(newsItem: NewsItem) {
        bookmarkViewModel.addBookmark(newsItem)
    }

    private fun bookmarkRemove(newsItem: NewsItem) {
        bookmarkViewModel.removeBookmark(newsItem)
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewBookmark.adapter = rVadapter
    }

    private fun navigateToDetails(newsItem: NewsItem) {
        val navController = findNavController()
        val action = SearchFragmentDirections.actionSearchFragmentToArticleFragment(newsItem)
        navController.navigate(action)
    }

    private fun observeBookmarks() {
        bookmarkViewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
            data = bookmarks
            rVadapter.updateData(data)
        }
    }
}