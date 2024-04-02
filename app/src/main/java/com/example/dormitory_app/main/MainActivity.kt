package com.example.dormitory_app.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.ActivityMainBinding
import com.example.dormitory_app.feature_login.presentation.LoginActivity
import com.example.dormitory_app.feature_profile.presentation.ProfileFragment
import com.example.dormitory_app.feature_profile.presentation.ProfileFragmentListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProfileFragmentListener {

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
                R.id.news -> replaceFragment(ProfileFragment()) // Предположим, у вас есть фрагмент для новостей
                else -> {
                    // Handle other cases if necessary
                }
            }
            true
        }

        // По умолчанию открываем первый элемент
        binding.bottomNavigationView.selectedItemId = R.id.person
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
