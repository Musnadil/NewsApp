package com.musnadil.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.musnadil.newsapp.databinding.ActivityMainBinding
import com.musnadil.newsapp.utility.appearanceWindow
import com.musnadil.newsapp.utility.hideSystemUI
import com.musnadil.newsapp.utility.setFullScreen
import com.musnadil.newsapp.utility.showSystemUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragmentContainer)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment -> {
                    setFullScreen(window,false)
                    hideSystemUI(window)
                }
                R.id.newsCategoriesFragment -> {
                    setFullScreen(window,true)
                    showSystemUI(window)
                    appearanceWindow(window, statusBarLight = false, bottomNavLight = false)
                }
                else -> {
                    setFullScreen(window,true)
                    showSystemUI(window)
                    appearanceWindow(window, statusBarLight = true, bottomNavLight = false)
                }
            }

        }
    }
}