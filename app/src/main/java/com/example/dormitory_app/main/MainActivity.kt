package com.example.dormitory_app.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.ActivityMainBinding
import com.example.dormitory_app.feature_booking_wm.ui.FragmentBooking
import com.example.dormitory_app.feature_login.presentation.LoginActivity
import com.example.dormitory_app.feature_news.presentation.NewsFragment
import com.example.dormitory_app.feature_profile.presentation.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentListener {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.person -> replaceFragment(ProfileFragment())
                R.id.news -> replaceFragment(NewsFragment()) // Предположим, у вас есть фрагмент для новостей
                R.id.bookingWM -> replaceFragment(FragmentBooking())
                else -> {
                    // Handle other cases if necessary
                }
            }
            true
        }

        binding.bottomNavigationView.selectedItemId = R.id.person
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
