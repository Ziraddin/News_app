package com.example.news_app.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.FragmentSearchBinding
import com.example.news_app.ui.MainActivity
import com.example.news_app.ui.adapter.NewsRVadapter
import com.example.news_app.ui.viewmodel.BookmarkViewModel
import com.example.news_app.ui.viewmodel.SearchSideEffect
import com.example.news_app.ui.viewmodel.SearchState
import com.example.news_app.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var rVadapter: NewsRVadapter
    lateinit var searchViewModel: SearchViewModel
    lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchViewModel = (activity as MainActivity).searchViewModel
        bookmarkViewModel = (activity as MainActivity).bookmarkViewModel
        rVadapter = NewsRVadapter(
            ::navigateToDetails, bookmarkAdd = ::bookmarkAdd, bookmarkRemove = ::bookmarkRemove
        )
        setUpRecyclerView()
        search()
        collectResults()
        return binding.root
    }


    private fun bookmarkAdd(newsItem: NewsItem) {
        bookmarkViewModel.addBookmark(newsItem)
    }

    private fun bookmarkRemove(newsItem: NewsItem) {
        bookmarkViewModel.removeBookmark(newsItem)
    }

    private fun navigateToDetails(newsItem: NewsItem) {
        val navController = findNavController()
        val action = SearchFragmentDirections.actionSearchFragmentToArticleFragment(newsItem)
        navController.navigate(action)
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewNews.adapter = rVadapter
    }

    private fun search() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchViewModel.searchNews(query.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })
    }

    private fun collectResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.searchState.collect { response ->
                        when (response) {
                            is SearchState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.recyclerViewNews.visibility = View.VISIBLE
                                rVadapter.submitList(response.result)
                            }

                            is SearchState.Loading -> {
                                binding.recyclerViewNews.visibility = View.INVISIBLE
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is SearchState.Error -> {
                                Toast.makeText(
                                    requireContext(), response.message, Toast.LENGTH_SHORT
                                ).show()
                            }

                            is SearchState.Idle -> {
                                println("Idle")
                            }
                        }
                    }
                }

                launch {
                    searchViewModel.searchSideEffect.collect { sideEffect ->
                        when (sideEffect) {
                            is SearchSideEffect.ShowNewsError -> {
                                Toast.makeText(
                                    requireContext(), sideEffect.message, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        collectResults()
    }
}