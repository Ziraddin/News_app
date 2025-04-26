package com.example.news_app.ui.fragment.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.FragmentArticleBinding


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        val newsItem: NewsItem = args.newsItem
        bindData(newsItem)

        return binding.root
    }

    private fun bindData(newsItem: NewsItem) {
        binding.titleTextView.text = newsItem.title
        binding.authorTextView.text = "By ${newsItem.author}"
        binding.publishedAtTextView.text = newsItem.publishedAt
        binding.descriptionTextView.text = newsItem.description
        binding.contentTextView.text = newsItem.content

        Glide.with(this)
            .load(newsItem.urlToImage)
            .into(binding.imageView)

        binding.urlTextView.text = newsItem.url

        binding.urlTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, newsItem.url.toUri())
            startActivity(intent)
        }
    }

}
