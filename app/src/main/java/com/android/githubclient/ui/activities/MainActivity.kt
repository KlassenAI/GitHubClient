package com.android.githubclient.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.githubclient.R
import com.android.githubclient.adapters.RepositoryListItemAdapter
import com.android.githubclient.db.RepositoryDatabase
import com.android.githubclient.repository.AppRepository
import com.android.githubclient.ui.fragments.DashboardFragment
import com.android.githubclient.ui.fragments.FavoritesFragment
import com.android.githubclient.ui.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var favoritesFragment: FavoritesFragment

    lateinit var favoritesAdapter: RepositoryListItemAdapter
    lateinit var searchAdapter: RepositoryListItemAdapter
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFields()
        initFunc()
    }

    private fun initFields() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.dashboardFragment -> replaceFragment(dashboardFragment)
                R.id.favoritesFragment -> replaceFragment(favoritesFragment)
            }
            true
        }

        dashboardFragment = DashboardFragment()
        favoritesFragment = FavoritesFragment()

        viewModel = MainViewModel(application)

        favoritesAdapter = RepositoryListItemAdapter(ArrayList(), this, viewModel)
        searchAdapter = RepositoryListItemAdapter(ArrayList(), this, viewModel)
    }

    private fun initFunc() {
        replaceFragment(dashboardFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}