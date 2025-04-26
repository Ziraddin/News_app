package com.example.news_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_app.R
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.NewsItemBinding

class NewsRVadapter(
    private var data: MutableList<NewsItem>,
    private val navigateToDetails: (NewsItem) -> Unit,
    private val bookmarkAdd: (NewsItem) -> Unit = {},
    private val bookmarkRemove: (NewsItem) -> Unit = {},
) : RecyclerView.Adapter<NewsRVadapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.newsTitle.text = newsItem.title

            Glide.with(binding.root).load(newsItem.urlToImage).into(binding.newsCoverImage)

            val iconRes = if (newsItem.isBookmarked) {
                R.drawable.bookmark_filled
            } else {
                R.drawable.bookmark_icon
            }
            binding.bookmarkIcon.setImageResource(iconRes)

            binding.bookmarkIcon.setOnClickListener {
                if (newsItem.isBookmarked) {
                    newsItem.isBookmarked = false
                    bookmarkRemove(newsItem)
                    binding.bookmarkIcon.setImageResource(R.drawable.bookmark_icon)
                } else {
                    newsItem.isBookmarked = true
                    bookmarkAdd(newsItem)
                    binding.bookmarkIcon.setImageResource(R.drawable.bookmark_filled)
                }
            }

            binding.root.setOnClickListener {
                navigateToDetails(newsItem)
            }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: NewsItem, newItem: NewsItem): Any? {
            return when {
                oldItem.isBookmarked != newItem.isBookmarked -> {

                }

                else -> null
            }
        }
    }

    val asyncListDiffer = AsyncListDiffer(this, diffCallback)

    fun submitList(newData: List<NewsItem>) {
        asyncListDiffer.submitList(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        data = asyncListDiffer.currentList
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}


