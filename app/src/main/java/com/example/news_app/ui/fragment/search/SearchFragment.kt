package com.example.news_app.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.FragmentSearchBinding
import com.example.news_app.ui.MainActivity
import com.example.news_app.ui.adapter.NewsRVadapter
import com.example.news_app.ui.viewmodel.SearchState
import com.example.news_app.ui.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var rVadapter: NewsRVadapter
    lateinit var searchViewModel: SearchViewModel
    private var data: List<NewsItem> = emptyList<NewsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchViewModel = (activity as MainActivity).searchViewModel
        rVadapter = NewsRVadapter(emptyList(), ::navigateToDetails)
        setUpRecyclerView()
        search()
        return binding.root
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
        binding.searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.searchNews(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        observeResults()
    }

    private fun observeResults() {
        searchViewModel.searchState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is SearchState.Success -> {
                    data = response.result
                    rVadapter.updateData(data)
                }

                is SearchState.Loading -> {
                    println("Loading")
                }

                is SearchState.Error -> {
                    println("Error: ${response.message}")
                }

                is SearchState.Idle -> {
                    println("Idle")
                }
            }
        }
    }
}