package com.example.news_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.news_app.R
import com.example.news_app.data.local.SharedPreferences
import com.example.news_app.data.repository.NewsRepository
import com.example.news_app.databinding.ActivityMainBinding
import com.example.news_app.ui.viewmodel.BookmarkViewModel
import com.example.news_app.ui.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        searchViewModel = SearchViewModel(NewsRepository())
        val sharedPreferences = SharedPreferences(this)
        bookmarkViewModel = BookmarkViewModel(sharedPreferences)

        //Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
    }
}