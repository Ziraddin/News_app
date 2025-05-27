package com.example.news_app.ui.fragment.article

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.news_app.R
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.FragmentArticleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        val newsItem: NewsItem = args.newsItem
        bindData(newsItem)
        hideNavbar()
        onBack()
        return binding.root
    }

    private fun hideNavbar() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindData(newsItem: NewsItem) {
        binding.titleTextView.text = newsItem.title
        binding.authorTextView.text = "By ${newsItem.author} • "
        binding.publishedAtTextView.text = formatDate(newsItem.publishedAt)
        binding.descriptionTextView.text = newsItem.description
        binding.contentTextView.text = newsItem.content

        Glide.with(this).load(newsItem.urlToImage).into(binding.imageView)

        binding.urlTextView.text = newsItem.url

        binding.urlTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, newsItem.url.toUri())
            startActivity(intent)
        }
    }

    private fun onBack() {
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(dateString: String): String {
        val parsedDate = ZonedDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
        return parsedDate.format(formatter)
    }

}
