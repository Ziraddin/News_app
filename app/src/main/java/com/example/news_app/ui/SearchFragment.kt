package com.example.news_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.news_app.R
import com.example.news_app.data.NewsItem
import com.example.news_app.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var rVadapter: NewsRVadapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        rVadapter = NewsRVadapter(emptyList())
        setGridRv()
        return binding.root
    }


    private fun setGridRv() {
        binding.recyclerViewNews.adapter = rVadapter
    }
}