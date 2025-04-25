package com.example.news_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news_app.R
import com.example.news_app.data.model.NewsItem
import com.example.news_app.databinding.NewsItemBinding

class NewsRVadapter(
    private var data: List<NewsItem>,
    private val navigateToDetails: (NewsItem) -> Unit,
    private val bookmarkAdd: (NewsItem) -> Unit = {},
    private val bookmarkRemove: (NewsItem) -> Unit = {},
) : RecyclerView.Adapter<NewsRVadapter.NewsViewHolder>() {


    inner class NewsViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.newsTitle.text = newsItem.title
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
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            navigateToDetails(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<NewsItem>) {
        data = newData
        notifyDataSetChanged()
    }
}